package io.gitee.dqcer.mcdull.framework.spi;

/**
 * 线程上下文传播器：把提交线程上的一类上下文带到执行线程。
 * <p>
 * 每个实现只负责一种上下文（登录态、日志 MDC、数据源路由栈……），由线程池按顺序收集全部实现，
 * 因此新增一种跨线程上下文不必再修改线程池代码。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public interface AsyncContextPropagator {

    /**
     * 从当前线程捕获上下文快照。
     * <p>
     * 返回的快照会被另一个线程读取，实现方必须做防御性拷贝：直接返回所属线程活着的
     * 可变对象，等于让两个线程并发读写同一个非线程安全容器。
     *
     * @return 上下文快照，当前线程无该上下文时返回 {@code null}
     */
    Object capture();

    /**
     * 把快照应用到当前线程。
     *
     * @param snapshot {@link #capture()} 返回的快照；为 {@code null} 时必须显式清空当前线程上下文，
     *                 否则执行线程会沿用上一个任务残留的值
     */
    void apply(Object snapshot);
}
