package io.gitee.mcdull.tools.config;

import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.web.component.ConcurrentRateLimiter;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.function.Supplier;

/**
 * Provides minimal framework beans required by BasicController, without needing Redis or DataSource.
 *
 * @author tools
 */
@Configuration
@EnableConfigurationProperties(McdullProperties.class)
public class ToolsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConcurrentRateLimiter concurrentRateLimiter() {
        return new NoOpConcurrentRateLimiter();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicLocaleMessageSource dynamicLocaleMessageSource() {
        return new SimpleDynamicLocaleMessageSource();
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * No-op rate limiter — always allows, no Redis needed.
     */
    private static class NoOpConcurrentRateLimiter implements ConcurrentRateLimiter {
        @Override
        public boolean limiter(String key, int quantity, int time) {
            return true;
        }

        @Override
        public <T> T locker(String key, long timeout, Supplier<T> function) {
            return function.get();
        }

        @Override
        public <T> void locker(String key, long timeout, Supplier<T> function, boolean throwException) {
            function.get();
        }
    }

    /**
     * Simple DynamicLocaleMessageSource that just returns the key.
     */
    private static class SimpleDynamicLocaleMessageSource implements DynamicLocaleMessageSource {
        @Override
        public String getMessage(String languageStr, String code, Object[] args) {
            return code;
        }

        @Override
        public String getMessage(String code, Object[] args) {
            return code;
        }

        @Override
        public String getMessage(String code) {
            return code;
        }
    }
}
