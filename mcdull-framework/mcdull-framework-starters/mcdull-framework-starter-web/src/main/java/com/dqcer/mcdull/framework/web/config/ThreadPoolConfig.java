package io.gitee.dqcer.framework.web.config;

import io.gitee.dqcer.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.framework.config.properties.McdullProperties;
import io.gitee.dqcer.framework.config.properties.ThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author dqcer
 * @date 2022/12/18
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Resource
    private McdullProperties mcdullProperties;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolProperties threadPool = mcdullProperties.getThreadPool();
        if (log.isDebugEnabled()) {
            log.debug("Init Thread Pool Config: {}", threadPool);
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPool.getCorePoolSize());
        executor.setMaxPoolSize(threadPool.getMaxPoolSize());
        executor.setKeepAliveSeconds(threadPool.getKeepAliveTime());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setThreadNamePrefix(GlobalConstant.ROOT_PREFIX + "-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();

        return executor;
    }
}
