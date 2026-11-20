package io.gitee.dqcer.mcdull.framework.scheduler.node;

import java.time.Instant;

/**
 * 定时任务执行上下文。
 *
 * <p>由 starter 在调用 {@link McdullScheduledTask#execute(TaskContext)} 时构造，
 * 对业务屏蔽底层调度器（db-scheduler）的具体类型，使任务实现只需依赖本 API 模块。
 *
 * @param <T> 任务状态类型，与 {@link McdullScheduledTask} 的泛型一致
 * @author dqcer
 * @since 1.0.0
 */
public interface TaskContext<T> {

    /**
     * 任务实例 ID（对应调度表中的 {@code task_instance}）。
     * <p>周期性任务通常为固定值，一次性任务为业务自定义的实例标识。
     */
    String taskId();

    /**
     * 当前持久化的状态数据，仅对有状态任务有效；无状态任务返回 {@code null}。
     */
    T data();

    /**
     * 本次执行的计划时间。可以在 {@link McdullScheduledTask#onFailure(Exception, TaskContext)}
     * 等回调中用于计算调度延迟，无法取得时返回 {@code null}。
     */
    Instant scheduledAt();

    /**
     * 本次是第几次执行（连续失败次数 + 1），至少为 1。
     */
    int attemptNumber();
}
