package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import lombok.extern.slf4j.Slf4j;

/**
 * 调度器告警门面。
 * <p>任务执行失败时触发告警。默认实现仅打日志，业务方可继承或 override 接钉钉/企微/邮件等。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class SchedulerAlarmService {

    /**
     * 任务执行失败时触发告警。
     *
     * @param taskName 任务名称
     * @param error    失败原因
     */
    public void onFailure(String taskName, Throwable error) {
        log.error("[mcdull-scheduler] 任务执行失败: {}, error: {}", taskName, error.getMessage(), error);
        // 默认仅打日志。业务方可 override 实现钉钉/企微/邮件告警。
    }

    /**
     * 任务执行超时时触发告警。
     *
     * @param taskName 任务名称
     * @param durationMs 执行耗时（毫秒）
     */
    public void onTimeout(String taskName, long durationMs) {
        log.warn("[mcdull-scheduler] 任务执行超时: {}, duration: {}ms", taskName, durationMs);
    }

    /**
     * 任务执行成功（可选告警场景，如执行耗时过长）。
     *
     * @param taskName   任务名称
     * @param durationMs 执行耗时（毫秒）
     */
    public void onSuccess(String taskName, long durationMs) {
        // 默认空实现。业务方可 override 实现耗时监控告警。
    }
}
