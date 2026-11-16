package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.ThreadPoolProperties;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author dqcer
 * @since 2022/12/18
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Resource
    private McdullProperties mcdullProperties;

    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolProperties threadPool = mcdullProperties.getThreadPool();
        LogHelp.info(log, "Init Thread Pool Config: {}", threadPool);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPool.getCorePoolSize());
        executor.setMaxPoolSize(threadPool.getMaxPoolSize());
        executor.setKeepAliveSeconds(threadPool.getKeepAliveTime());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setThreadNamePrefix(GlobalConstant.ROOT_PREFIX + "-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 传递上下文
        executor.setTaskDecorator(new ThreadPoolDecorator());

        // 优雅停机配置：等待任务完成，并设置最大阻塞等待时间（建议后续抽离到 ThreadPoolProperties 中配置）
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }

    /**
     * 显式绑定 @Async 默认使用的线程池，防止多 Executor Bean 场景下退化为 SimpleAsyncTaskExecutor
     */
    @Override
    public Executor getAsyncExecutor() {
        return threadPoolTaskExecutor();
    }

    /**
     * 统一处理无返回值 @Async 方法抛出的未捕获异常
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
            LogHelp.error(log, "Async method [{}] execution error, params: {}", method.getName(), params, ex);
    }

    /**
     * 线程池装饰 -> 解决异步多线程中传递上下文
     * <p>
     * 采用「捕获-替换-还原」而不是原来的「捕获-替换-清空」：{@code CallerRunsPolicy} 在队列打满时会让任务
     * 退化为在提交线程上同步执行，此时清空会连带抹掉提交线程自己的 traceId、登录态和数据源路由栈——请求后半段
     * 丢日志链路，动态数据源静默回落默认库。还原成执行线程自己的前值，在池线程与提交线程两种执行位置都成立。
     *
     * @author dqcer
     * @since 2023/05/19
     */
    static class ThreadPoolDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            ContextSnapshot captured = ContextSnapshot.capture();
            return () -> {
                ContextSnapshot executorOwn = ContextSnapshot.capture();
                captured.apply();
                try {
                    runnable.run();
                } catch (Throwable t) {
                    // 仅记录底层异常，务必原样抛出，避免吞掉异常导致 CompletableFuture / Future.get() 无法感知失败
                    LogHelp.error(log, "ThreadPoolDecorator task execution error.", t);
                    throw t;
                } finally {
                    executorOwn.apply();
                }
            };
        }
    }

    /**
     * 线程上下文快照。
     * <p>
     * 三个上下文都是可变共享对象，捕获时一律做防御性拷贝：
     * {@code DynamicContextHolder.getAll()} 返回的是所属线程活着的 Deque，跨线程直接复用等于两个线程并发
     * push/poll 同一个非线程安全的 {@code ArrayDeque}，异步任务里的数据源变更还会倒灌回请求线程。
     */
    private record ContextSnapshot(UnifySession session, Map<String, String> mdc, Deque<String> dataSources) {

        static ContextSnapshot capture() {
            // 注：如果 UnifySession 存在可变属性且异步线程会修改它，建议在此处调用 session.copy()
            return new ContextSnapshot(
                UserContextHolder.getSession(),
                MDC.getCopyOfContextMap(),
                new ArrayDeque<>(DynamicContextHolder.getAll())
            );
        }

        void apply() {
            if (session == null) {
                UserContextHolder.clearSession();
            } else {
                UserContextHolder.setSession(session);
            }

            // 快照为空时也要显式清空，否则执行线程会沿用上一个任务残留的 traceId
            if (mdc == null || mdc.isEmpty()) {
                MDC.clear();
            } else {
                MDC.setContextMap(mdc);
            }

            DynamicContextHolder.setAll(new ArrayDeque<>(dataSources));
        }
    }
}
