package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TaskQueryService 默认 JdbcTemplate 实现。
 * <p>通过直接查询 db-scheduler 的 {@code scheduled_tasks} 表实现查询能力。
 * <p>由 {@code SchedulerAutoConfiguration} 注册，数据源为调度表库 DataSource。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class DefaultTaskQueryService implements TaskQueryService {

    private static final String SELECT_ALL_TASKS =
            "SELECT task_name, task_instance, execution_time, picked, picked_by, " +
            "last_success, last_failure, consecutive_failures, last_heartbeat " +
            "FROM scheduled_tasks ORDER BY execution_time ASC";

    private static final String SELECT_TASK_HISTORY =
            "SELECT task_name, task_instance, execution_time, picked, picked_by, " +
            "last_success, last_failure, consecutive_failures, last_heartbeat " +
            "FROM scheduled_tasks WHERE task_name = ? ORDER BY execution_time DESC LIMIT ?";

    private static final String SELECT_NEXT_EXECUTE_TIME =
            "SELECT execution_time FROM scheduled_tasks " +
            "WHERE task_name = ? AND execution_time >= NOW() " +
            "ORDER BY execution_time ASC LIMIT 1";

    private final JdbcTemplate jdbcTemplate;

    /**
     * 执行历史仓储，未开启 {@code mcdull.scheduler.history.enabled} 时为 null
     */
    private final TaskHistoryRepository historyRepository;

    public DefaultTaskQueryService(JdbcTemplate jdbcTemplate, TaskHistoryRepository historyRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.historyRepository = historyRepository;
    }

    @Override
    public List<TaskExecutionVO> listTasks() {
        try {
            return jdbcTemplate.query(SELECT_ALL_TASKS, (rs, row) ->
                    TaskExecutionVO.builder()
                            .taskName(rs.getString("task_name"))
                            .taskInstance(rs.getString("task_instance"))
                            .executionTime(toInstant(rs.getTimestamp("execution_time")))
                            .picked(rs.getBoolean("picked"))
                            .pickedBy(rs.getString("picked_by"))
                            .lastSuccess(toInstant(rs.getTimestamp("last_success")))
                            .lastFailure(toInstant(rs.getTimestamp("last_failure")))
                            .consecutiveFailures(rs.getInt("consecutive_failures"))
                            .lastHeartbeat(toInstant(rs.getTimestamp("last_heartbeat")))
                            .build()
            );
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 查询 scheduled_tasks 失败（表可能尚未创建）: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<TaskHistoryVO> history(String taskName, int limit) {
        try {
            return jdbcTemplate.query(SELECT_TASK_HISTORY,
                    new Object[]{taskName, limit},
                    (rs, row) -> {
                        Instant executionTime = toInstant(rs.getTimestamp("execution_time"));
                        Instant lastSuccess = toInstant(rs.getTimestamp("last_success"));
                        Instant lastFailure = toInstant(rs.getTimestamp("last_failure"));
                        boolean picked = rs.getBoolean("picked");
                        int consecutiveFailures = rs.getInt("consecutive_failures");

                        String status;
                        if (picked) {
                            status = "RUNNING";
                        } else if (consecutiveFailures > 0) {
                            status = "FAILED";
                        } else if (lastSuccess != null) {
                            status = "SUCCESS";
                        } else {
                            status = "PENDING";
                        }

                        return TaskHistoryVO.builder()
                                .taskName(rs.getString("task_name"))
                                .taskInstance(rs.getString("task_instance"))
                                .executionTime(executionTime)
                                .status(status)
                                .successTime(lastSuccess)
                                .failureTime(lastFailure)
                                .consecutiveFailures(consecutiveFailures)
                                .build();
                    }
            );
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 查询任务历史失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Instant nextExecuteTime(String taskName) {
        try {
            Timestamp ts = jdbcTemplate.queryForObject(SELECT_NEXT_EXECUTE_TIME,
                    new Object[]{taskName}, Timestamp.class);
            return ts != null ? ts.toInstant() : null;
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 查询下次执行时间失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<TaskExecutionRecord> executions(String taskName, String status, int limit) {
        if (historyRepository == null) {
            return Collections.emptyList();
        }
        return historyRepository.query(taskName, status, limit);
    }

    private static Instant toInstant(Timestamp ts) {
        return ts != null ? ts.toInstant() : null;
    }
}
