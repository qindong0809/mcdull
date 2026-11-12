package io.gitee.dqcer.mcdull.framework.scheduler.node;

import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.TaskInstance;

/**
 * 定时任务接口。
 *
 * <p>适合复杂场景：需要 ExecutionContext、StatefulTask、链式调度、动态读取配置等。
 * 简单场景建议直接使用 {@code @McdullScheduled} 注解。
 *
 * <p>接口设计参考 {@code ProcessHandler}：两个核心方法 + 多个默认方法，子类按需 override。
 *
 * @param <T> 任务状态类型。{@code Void} 表示无状态。
 * @author dqcer
 * @since 1.0.0
 */
public interface McdullScheduledTask<T> {

    /**
     * 返回调度定义。
     * <p>可以在方法内部动态读取配置（如 Nacos），支持运行时刷新 cron 等参数。
     */
    McdullScheduleDefinition definition();

    /**
     * 任务执行逻辑。
     *
     * @param instance 任务实例，包含实例 ID 和持久化的 data（StatefulTask 时）
     * @param context  执行上下文，可用于链式调度、持久化状态等
     */
    void execute(TaskInstance<T> instance, ExecutionContext context);

    // ========== 默认方法，按需 override ==========

    /**
     * 是否为有状态任务。
     * <p>有状态任务执行后可通过 {@code context.persist(data)} 更新持久化状态，
     * 下次执行时通过 {@code instance.getData()} 拿到。
     */
    default boolean isStateful() {
        return false;
    }

    /**
     * 任务数据类型。StatefulTask 时指定。
     */
    @SuppressWarnings("unchecked")
    default Class<T> dataType() {
        return (Class<T>) Void.class;
    }

    /**
     * 任务执行失败回调。
     * <p>默认实现仅打印日志。子类可 override 实现钉钉/企微告警等。
     */
    default void onFailure(Exception e, TaskInstance<T> instance, ExecutionContext context) {
        // 默认空实现，日志由拦截器统一处理
    }

    /**
     * 任务执行成功回调。
     */
    default void onSuccess(TaskInstance<T> instance, ExecutionContext context) {
        // 默认空实现
    }
}
