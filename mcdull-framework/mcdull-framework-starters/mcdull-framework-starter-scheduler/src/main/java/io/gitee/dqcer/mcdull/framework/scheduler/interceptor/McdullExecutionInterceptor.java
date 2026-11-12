package io.gitee.dqcer.mcdull.framework.scheduler.interceptor;

import io.gitee.dqcer.mcdull.framework.scheduler.operation.SchedulerAlarmService;
import io.gitee.dqcer.mcdull.framework.scheduler.operation.TaskHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Instant;

/**
 * 通用执行拦截器：TraceId 注入、耗时统计、统一异常处理、执行历史记录、McdullScheduledTask 回调。
 * <p>包装 db-scheduler 的 ExecutionInterceptor。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class McdullExecutionInterceptor {

    private static final String TRACE_ID_KEY = "traceId";

    private final SchedulerAlarmService alarmService;

    /**
     * 执行历史仓储，未开启 {@code mcdull.scheduler.history.enabled} 时为 null
     */
    private final TaskHistoryRepository historyRepository;

    public McdullExecutionInterceptor(SchedulerAlarmService alarmService,
                                      TaskHistoryRepository historyRepository) {
        this.alarmService = alarmService;
        this.historyRepository = historyRepository;
    }

    /**
     * 执行前处理：注入 TraceId，并写入一条 RUNNING 执行记录。
     *
     * @return 执行记录主键；未开启历史或写入失败时返回 null
     */
    public Long beforeExecution(String taskName, String instanceId, Instant scheduledAt, int attemptNumber) {
        // TraceId 注入（如果 MDC 里没有则生成一个）
        String traceId = MDC.get(TRACE_ID_KEY);
        if (traceId == null || traceId.isEmpty()) {
            traceId = "scheduler-" + taskName + "-" + instanceId + "-" + System.currentTimeMillis();
        }
        MDC.put(TRACE_ID_KEY, traceId);
        log.debug("[mcdull-scheduler] 开始执行任务: {}, instance: {}", taskName, instanceId);

        if (historyRepository == null) {
            return null;
        }
        return historyRepository.insertRunning(taskName, instanceId, scheduledAt, attemptNumber, traceId);
    }

    /**
     * 执行后处理：耗时统计、告警、回写执行结果。
     */
    public void afterExecution(Long recordId, String taskName, String instanceId,
                               long durationMs, boolean success, Exception error) {
        try {
            if (success) {
                log.debug("[mcdull-scheduler] 任务执行成功: {}, instance: {}, duration: {}ms",
                        taskName, instanceId, durationMs);
                alarmService.onSuccess(taskName, durationMs);
                if (historyRepository != null) {
                    historyRepository.markSuccess(recordId);
                }
            } else {
                log.error("[mcdull-scheduler] 任务执行失败: {}, instance: {}, duration: {}ms, error: {}",
                        taskName, instanceId, durationMs, error != null ? error.getMessage() : "unknown");
                alarmService.onFailure(taskName, error);
                if (historyRepository != null) {
                    historyRepository.markFailed(recordId, error);
                }
            }
        } finally {
            // 执行线程池会复用线程，必须保证清理，否则下一个任务会读到上一个任务的 traceId
            MDC.remove(TRACE_ID_KEY);
        }
    }
}
