package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.ThreadPoolProperties;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.datasource.GlobalDataRoutingDataSource;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
public class ThreadPoolConfig {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Resource
    private McdullProperties mcdullProperties;

    @Resource
    private GlobalDataRoutingDataSource globalDataRoutingDataSource;

    @Bean
    public Executor threadPoolTaskExecutor(){
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
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();

        return executor;
    }

    /**
     * 线程池装饰->解决 异步多线程中传递上下文
     *
     * @author dqcer
     * @since  2023/05/19
     */
    static class ThreadPoolDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            UnifySession session = UserContextHolder.getSession();
            Map<String,String> previous = MDC.getCopyOfContextMap();
            Deque<String> deque = DynamicContextHolder.getAll();
            return () -> {
                try {
                    UserContextHolder.setSession(session);
                    if (previous != null && !previous.isEmpty()) {
                        MDC.setContextMap(previous);
                    }
                    DynamicContextHolder.setAll(deque);
                    runnable.run();
                } catch (Exception e) {
                    LogHelp.error(log, "ThreadPoolDecorator error.", e);
                } finally {
                    DynamicContextHolder.clear();
                    UserContextHolder.clearSession();
                    MDC.clear();
                }
            };
        }
    }
}
