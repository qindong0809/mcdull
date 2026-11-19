package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.ThreadPoolProperties;
import io.gitee.dqcer.mcdull.framework.web.context.AsyncContextSupport;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author dqcer
 * @since 2022-12-18
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Resource
    private McdullProperties mcdullProperties;
    @Resource
    private ObjectProvider<List<TaskDecorator>> taskDecoratorsProvider;
    @Resource
    private AsyncContextSupport asyncContextSupport;

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

        // 传入扩展钩子列表
        // 收集容器内其它 TaskDecorator bean，供应用做非上下文类装饰（改写任务、包装异常等）；
        // 跨线程传递上下文请贡献 AsyncContextPropagator，由下面的 AsyncContextSupport 统一捕获-回放-还原
        List<TaskDecorator> externalDecorators = taskDecoratorsProvider.getIfAvailable(Collections::emptyList);
        executor.setTaskDecorator(new ThreadPoolDecorator(externalDecorators, asyncContextSupport));

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
     * 上下文本身由容器收集的 {@code AsyncContextPropagator} 决定，线程池只负责在提交线程捕获、
     * 在执行线程回放并在结束后还原，因此新增一种跨线程上下文不需要改动这里。
     *
     * @author dqcer
     * @since 2023-05-19
     */
    static class ThreadPoolDecorator implements TaskDecorator {

        private final List<TaskDecorator> delegates;

        private final AsyncContextSupport asyncContextSupport;

        ThreadPoolDecorator(List<TaskDecorator> delegates, AsyncContextSupport asyncContextSupport) {
            this.delegates = delegates;
            this.asyncContextSupport = asyncContextSupport;
        }

        @Override
        public Runnable decorate(Runnable runnable) {
            // 逐层调用外部 TaskDecorator 包装（在提交线程捕获各自上下文）
            Runnable wrappedTask = runnable;
            for (TaskDecorator delegate : delegates) {
                wrappedTask = delegate.decorate(wrappedTask);
            }
            return asyncContextSupport.wrap(wrappedTask);
        }
    }
}
