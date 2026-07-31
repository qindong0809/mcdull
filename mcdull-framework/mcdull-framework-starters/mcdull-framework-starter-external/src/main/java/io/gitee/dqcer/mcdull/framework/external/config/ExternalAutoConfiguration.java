package io.gitee.dqcer.mcdull.framework.external.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for external system integration.
 * Activates when {@code mcdull.external.clients} is present in configuration.
 *
 * @author dqcer
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ExternalClientProperties.class)
public class ExternalAutoConfiguration {
}
