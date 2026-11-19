package io.gitee.dqcer.mcdull.framework.web.context;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;
import io.gitee.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.function.Supplier;

/**
 * 线程上下文传播引擎。
 * <p>
 * 采用「捕获-替换-还原」而不是原来的「捕获-替换-清空」：{@code CallerRunsPolicy} 在队列打满时会让任务
 * 退化为在提交线程上同步执行，此时清空会连带抹掉提交线程自己的 traceId、登录态和数据源路由栈——请求后半段
 * 丢日志链路，动态数据源静默回落默认库。还原成执行线程自己的前值，在池线程与提交线程两种执行位置都成立。
 * <p>
 * 具体传递哪些上下文由容器中的 {@link AsyncContextPropagator} 决定，新增一种上下文只需再贡献一个实现，
 * 不需要改动线程池。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public class AsyncContextSupport {

    private static final Logger log = LoggerFactory.getLogger(AsyncContextSupport.class);

    /**
     * 容器尚未就绪（如静态工具在无 Spring 环境被调用）时的兜底：原样执行，不传递上下文
     */
    private static final AsyncContextSupport UNPROPAGATED = new AsyncContextSupport(List.of());

    private final List<AsyncContextPropagator> propagators;

    public AsyncContextSupport(List<AsyncContextPropagator> propagators) {
        this.propagators = List.copyOf(propagators);
    }

    /**
     * 供无法注入 bean 的静态入口取用当前应用的实例
     *
     * @return {@link AsyncContextSupport}
     */
    public static AsyncContextSupport current() {
        ApplicationContext context = SpringContextHolder.getContext();
        if (context == null) {
            return UNPROPAGATED;
        }
        AsyncContextSupport support = context.getBeanProvider(AsyncContextSupport.class).getIfAvailable();
        return support == null ? UNPROPAGATED : support;
    }

    /**
     * 捕获当前线程的全部上下文快照
     *
     * @return 与各传播器顺序一一对应的快照数组
     */
    public Object capture() {
        Object[] snapshots = new Object[propagators.size()];
        for (int i = 0; i < snapshots.length; i++) {
            snapshots[i] = propagators.get(i).capture();
        }
        return snapshots;
    }

    /**
     * 把快照回放到当前线程
     *
     * @param snapshot {@link #capture()} 的返回值
     */
    public void apply(Object snapshot) {
        Object[] snapshots = (Object[]) snapshot;
        for (int i = 0; i < propagators.size(); i++) {
            propagators.get(i).apply(snapshots[i]);
        }
    }

    /**
     * 包装任务，使其在执行线程上先看得到提交线程的上下文，执行完再还原执行线程自己的前值
     *
     * @param runnable 任务
     * @return {@link Runnable}
     */
    public Runnable wrap(Runnable runnable) {
        Object captured = capture();
        return () -> {
            Object executorOwn = capture();
            apply(captured);
            try {
                runnable.run();
            } catch (Throwable t) {
                LogHelp.error(log, "Async task execution error.", t);
                throw t;
            } finally {
                apply(executorOwn);
            }
        };
    }

    /**
     * 包装带返回值的任务
     *
     * @param supplier 任务
     * @return {@link Supplier}
     */
    public <T> Supplier<T> wrap(Supplier<T> supplier) {
        Object captured = capture();
        return () -> {
            Object executorOwn = capture();
            apply(captured);
            try {
                return supplier.get();
            } catch (Throwable t) {
                LogHelp.error(log, "Async task execution error.", t);
                throw t;
            } finally {
                apply(executorOwn);
            }
        };
    }
}
