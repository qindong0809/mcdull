package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 任务执行历史仓储。
 * <p>落在调度表库（与 {@code scheduled_tasks} 同库），提供写入、查询、过期清理能力。
 * <p><b>容错约定</b>：记录历史失败绝不能影响任务本身的执行结果，所有写操作内部吞掉异常并打印 warn 日志。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class TaskHistoryRepository {

    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";

    /**
     * 执行状态未知：进程异常退出导致 RUNNING 记录未能回写
     */
    public static final String STATUS_LOST = "LOST";

    /**
     * 失败信息最大长度，超出部分截断，避免大段堆栈把行撑爆
     */
    private static final int FAILURE_MESSAGE_MAX_LENGTH = 2000;

    private static final String INSERT_RUNNING = """
            INSERT INTO scheduler_task_execution
                (task_name, task_instance, scheduled_at, started_at, status, executor_node, attempt_number, trace_id, created_time)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_COMPLETED = """
            UPDATE scheduler_task_execution
               SET status = ?, completed_at = ?, failure_message = ?
             WHERE id = ?
            """;

    private static final String SELECT_BASE = """
            SELECT id, task_name, task_instance, scheduled_at, started_at, completed_at,
                   status, executor_node, failure_message, attempt_number, trace_id, created_time
              FROM scheduler_task_execution
            """;

    private static final String DELETE_EXPIRED = "DELETE FROM scheduler_task_execution WHERE started_at < ?";

    private static final String MARK_LOST = """
            UPDATE scheduler_task_execution
               SET status = ?, failure_message = ?, completed_at = ?
             WHERE status = ? AND started_at < ?
            """;

    private static final String NODE = resolveNode();

    private final JdbcTemplate jdbcTemplate;

    private final int retentionDays;

    public TaskHistoryRepository(JdbcTemplate jdbcTemplate, int retentionDays) {
        this.jdbcTemplate = jdbcTemplate;
        this.retentionDays = retentionDays;
    }

    /**
     * 执行开始时插入一条 {@code RUNNING} 记录。
     *
     * @param taskName      任务名称
     * @param instanceId    任务实例
     * @param scheduledAt   计划执行时间，可为 null
     * @param attemptNumber 尝试次数
     * @param traceId       链路追踪 ID
     * @return 记录主键；写入失败时返回 null（不影响任务执行）
     */
    public Long insertRunning(String taskName,
                              String instanceId,
                              Instant scheduledAt,
                              int attemptNumber,
                              String traceId) {
        try {
            Timestamp now = Timestamp.from(Instant.now());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_RUNNING,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, taskName);
                ps.setString(2, instanceId);
                ps.setTimestamp(3, toTimestamp(scheduledAt));
                ps.setTimestamp(4, now);
                ps.setString(5, STATUS_RUNNING);
                ps.setString(6, NODE);
                ps.setInt(7, attemptNumber);
                ps.setString(8, traceId);
                ps.setTimestamp(9, now);
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            return key == null ? null : key.longValue();
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 写入任务执行记录失败（不影响任务执行）: task={}, error={}",
                    taskName, e.getMessage());
            return null;
        }
    }

    /**
     * 标记执行成功。
     */
    public void markSuccess(Long recordId) {
        markCompleted(recordId, STATUS_SUCCESS, null);
    }

    /**
     * 标记执行失败。
     */
    public void markFailed(Long recordId, Throwable error) {
        markCompleted(recordId, STATUS_FAILED, formatFailure(error));
    }

    /**
     * 查询执行历史。
     *
     * @param taskName 任务名称，为 null 表示不过滤
     * @param status   执行状态，为 null 表示不过滤
     * @param limit    最多返回条数
     */
    public List<TaskExecutionRecord> query(String taskName, String status, int limit) {
        try {
            StringBuilder sql = new StringBuilder(SELECT_BASE).append(" WHERE 1 = 1");
            List<Object> args = new ArrayList<>(4);
            if (StringUtils.hasText(taskName)) {
                sql.append(" AND task_name = ?");
                args.add(taskName);
            }
            if (StringUtils.hasText(status)) {
                sql.append(" AND status = ?");
                args.add(status);
            }
            sql.append(" ORDER BY started_at DESC LIMIT ?");
            args.add(limit);

            return jdbcTemplate.query(sql.toString(), args.toArray(),
                    (rs, row) -> TaskExecutionRecord.builder()
                            .id(rs.getLong("id"))
                            .taskName(rs.getString("task_name"))
                            .taskInstance(rs.getString("task_instance"))
                            .scheduledAt(toInstant(rs.getTimestamp("scheduled_at")))
                            .startedAt(toInstant(rs.getTimestamp("started_at")))
                            .completedAt(toInstant(rs.getTimestamp("completed_at")))
                            .status(rs.getString("status"))
                            .executorNode(rs.getString("executor_node"))
                            .failureMessage(rs.getString("failure_message"))
                            .attemptNumber(rs.getInt("attempt_number"))
                            .traceId(rs.getString("trace_id"))
                            .createdTime(toInstant(rs.getTimestamp("created_time")))
                            .build());
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 查询任务执行历史失败（表可能未启用）: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 将超时仍处于 {@code RUNNING} 的记录标记为 {@code LOST}。
     * <p>典型场景：进程被强制 kill，执行前插入的 RUNNING 记录没有机会回写。
     *
     * @param timeoutMs 判定超时时间（毫秒），应大于任务最长执行时间
     * @return 更新行数
     */
    public int markLost(long timeoutMs) {
        if (timeoutMs <= 0) {
            return 0;
        }
        try {
            Timestamp now = Timestamp.from(Instant.now());
            Timestamp threshold = Timestamp.from(Instant.now().minusMillis(timeoutMs));
            int updated = jdbcTemplate.update(MARK_LOST, STATUS_LOST,
                    "任务执行状态未回写，进程可能异常退出", now, STATUS_RUNNING, threshold);
            if (updated > 0) {
                log.warn("[mcdull-scheduler] 已标记 {} 条超时未回写的执行记录为 LOST", updated);
            }
            return updated;
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 标记 LOST 执行记录失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 删除超过保留天数的执行记录。
     *
     * @return 删除行数
     */
    public int purgeExpired() {
        if (retentionDays <= 0) {
            return 0;
        }
        try {
            Timestamp threshold = Timestamp.from(Instant.now().minus(retentionDays, ChronoUnit.DAYS));
            int deleted = jdbcTemplate.update(DELETE_EXPIRED, threshold);
            if (deleted > 0) {
                log.info("[mcdull-scheduler] 已清理过期执行记录 {} 条（保留 {} 天）", deleted, retentionDays);
            }
            return deleted;
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 清理过期执行记录失败: {}", e.getMessage());
            return 0;
        }
    }

    private void markCompleted(Long recordId, String status, String failureMessage) {
        if (recordId == null) {
            return;
        }
        try {
            jdbcTemplate.update(UPDATE_COMPLETED, status,
                    Timestamp.from(Instant.now()),
                    failureMessage,
                    recordId);
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 更新任务执行记录失败（不影响任务执行）: id={}, error={}",
                    recordId, e.getMessage());
        }
    }

    private static String formatFailure(Throwable error) {
        if (error == null) {
            return null;
        }
        String message = error.getClass().getName() + ": " + error.getMessage();
        if (message.length() > FAILURE_MESSAGE_MAX_LENGTH) {
            return message.substring(0, FAILURE_MESSAGE_MAX_LENGTH);
        }
        return message;
    }

    private static String resolveNode() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static Timestamp toTimestamp(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }

    private static Instant toInstant(Timestamp ts) {
        return ts == null ? null : ts.toInstant();
    }
}
