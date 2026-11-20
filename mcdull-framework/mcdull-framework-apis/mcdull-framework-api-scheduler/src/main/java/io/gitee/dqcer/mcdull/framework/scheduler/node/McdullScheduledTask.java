package io.gitee.dqcer.mcdull.framework.scheduler.node;

/**
 * 定时任务接口。
 *
 * <p>适合复杂场景：动态读取配置、有状态任务、任务回调等。
 * 简单场景建议直接使用 {@code @McdullScheduled} 注解。
 *
 * <p>接口设计参考 {@code ProcessHandler}：两个核心方法 + 多个默认方法，子类按需 override。
 * 执行上下文使用本模块自有的 {@link TaskContext}，不暴露任何底层调度器类型。
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
     * @param context 本次执行上下文，提供实例 ID、持久化状态与执行诊断信息
     */
    void execute(TaskContext<T> context);

    // ========== 默认方法，按需 override ==========

    /**
     * 是否为有状态任务。
     * <p>有状态任务的数据由底层调度器持久化，下次执行时可通过 {@link TaskContext#data()} 拿到。
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
    default void onFailure(Exception e, TaskContext<T> context) {
        // 默认空实现，日志由拦截器统一处理
    }

    /**
     * 任务执行成功回调。
     */
    default void onSuccess(TaskContext<T> context) {
        // 默认空实现
    }
}
