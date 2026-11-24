package io.gitee.dqcer.mcdull.framework.scheduler.config;

import io.gitee.dqcer.mcdull.framework.scheduler.health.SchedulerTableHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 调度表健康检查的条件装配。
 * <p>
 * 仅在 classpath 存在 Actuator 时生效，未引入 actuator 的服务不受影响。
 *
 * @author dqcer
 * @since 1.0.0
 */
@AutoConfiguration(after = SchedulerAutoConfiguration.class)
@ConditionalOnClass(name = "org.springframework.boot.actuate.health.HealthIndicator")
@ConditionalOnProperty(name = "mcdull.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class SchedulerHealthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchedulerTableHealthIndicator schedulerTableHealthIndicator(
            SchedulerDataSourceProvider dataSourceProvider,
            SchedulerProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceProvider.getDataSource());
        return new SchedulerTableHealthIndicator(jdbcTemplate, properties.getHistory().isEnabled());
    }
}
