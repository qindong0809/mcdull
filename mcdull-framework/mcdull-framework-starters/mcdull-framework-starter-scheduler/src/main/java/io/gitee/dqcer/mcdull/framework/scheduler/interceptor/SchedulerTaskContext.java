package io.gitee.dqcer.mcdull.framework.scheduler.interceptor;

import com.github.kagkarlsson.scheduler.task.Execution;
import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import io.gitee.dqcer.mcdull.framework.scheduler.node.TaskContext;

import java.time.Instant;

/**
 * 把 db-scheduler 的 {@link TaskInstance} + {@link ExecutionContext} 适配为 mcdull 自有
 * {@link TaskContext}，使业务侧只面向 API 契约，不接触任何 db-scheduler 类型。
 *
 * <p>仅存在于 starter 内部，由 {@link TaskConverter} 在调用任务回调前构造。
 *
 * @author dqcer
 * @since 1.0.0
 */
final class SchedulerTaskContext<T> implements TaskContext<T> {

    private final TaskInstance<T> instance;
    private final ExecutionContext context;

    SchedulerTaskContext(TaskInstance<T> instance, ExecutionContext context) {
        this.instance = instance;
        this.context = context;
    }

    @Override
    public String taskId() {
        return instance == null ? null : instance.getId();
    }

    @Override
    public T data() {
        return instance == null ? null : instance.getData();
    }

    @Override
    public Instant scheduledAt() {
        Execution execution = context == null ? null : context.getExecution();
        return execution == null ? null : execution.getExecutionTime();
    }

    @Override
    public int attemptNumber() {
        Execution execution = context == null ? null : context.getExecution();
        return execution == null ? 1 : execution.consecutiveFailures + 1;
    }
}
