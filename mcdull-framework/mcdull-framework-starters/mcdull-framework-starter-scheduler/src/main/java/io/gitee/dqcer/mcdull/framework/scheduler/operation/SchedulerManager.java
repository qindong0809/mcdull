package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.SchedulerClient;
import com.github.kagkarlsson.scheduler.task.TaskInstanceId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import java.time.Instant;

/**
 * 调度器管理门面。
 * <p>封装 db-scheduler 的 Scheduler 和 SchedulerClient，提供 pause / resume / trigger / cancel 等管理能力。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class SchedulerManager {

    /** RecurringTask 的默认实例 ID */
    private static final String DEFAULT_INSTANCE_ID = "RECURRING";

    private final ObjectProvider<Scheduler> schedulerProvider;
    private final ObjectProvider<SchedulerClient> schedulerClientProvider;

    public SchedulerManager(ObjectProvider<Scheduler> schedulerProvider,
                            ObjectProvider<SchedulerClient> schedulerClientProvider) {
        this.schedulerProvider = schedulerProvider;
        this.schedulerClientProvider = schedulerClientProvider;
    }

    /**
     * 手动触发一次任务（立即执行，不管 cron 表达式）。
     *
     * @param taskName 任务名称
     */
    public void trigger(String taskName) {
        log.info("[mcdull-scheduler] 手动触发任务: {}", taskName);
        try {
            schedulerClient().reschedule(TaskInstanceId.of(taskName, DEFAULT_INSTANCE_ID), Instant.now());
        } catch (Exception e) {
            log.error("[mcdull-scheduler] 手动触发任务失败: {}", taskName, e);
            throw new RuntimeException("手动触发任务失败: " + taskName, e);
        }
    }

    /**
     * 取消任务（下次不会再调度，直到重新触发）。
     *
     * @param taskName 任务名称
     */
    public void cancel(String taskName) {
        log.info("[mcdull-scheduler] 取消任务: {}", taskName);
        try {
            schedulerClient().cancel(TaskInstanceId.of(taskName, DEFAULT_INSTANCE_ID));
        } catch (Exception e) {
            log.error("[mcdull-scheduler] 取消任务失败: {}", taskName, e);
            throw new RuntimeException("取消任务失败: " + taskName, e);
        }
    }

    /**
     * 暂停整个调度器。
     */
    public void pause() {
        log.info("[mcdull-scheduler] 暂停整个调度器");
        scheduler().pause();
    }

    /**
     * 恢复调度器。
     */
    public void resume() {
        log.info("[mcdull-scheduler] 恢复调度器");
        scheduler().resume();
    }

    /**
     * 暂停单个任务（通过 cancel 实现）。
     *
     * @param taskName 任务名称
     */
    public void pauseTask(String taskName) {
        log.info("[mcdull-scheduler] 暂停任务（cancel）: {}", taskName);
        cancel(taskName);
    }

    /**
     * 恢复已暂停的单个任务（通过重新调度实现）。
     *
     * @param taskName 任务名称
     */
    public void resumeTask(String taskName) {
        log.info("[mcdull-scheduler] 恢复任务（reschedule now）: {}", taskName);
        trigger(taskName);
    }

    /**
     * 获取底层 Scheduler 实例（高级场景使用）。
     */
    public Scheduler scheduler() {
        return schedulerProvider.getObject();
    }

    /**
     * 获取底层 SchedulerClient 实例（高级场景使用）。
     */
    public SchedulerClient schedulerClient() {
        return schedulerClientProvider.getObject();
    }
}
