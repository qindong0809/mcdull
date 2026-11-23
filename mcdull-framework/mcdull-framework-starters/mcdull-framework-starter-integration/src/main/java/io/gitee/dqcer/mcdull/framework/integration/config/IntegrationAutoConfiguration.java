package io.gitee.dqcer.mcdull.framework.integration.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration for integration system clients.
 * Activates when {@code mcdull.integration.clients} is present in configuration.
 *
 * @author dqcer
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "mcdull.integration", name = "clients")
@EnableConfigurationProperties(IntegrationClientProperties.class)
@Import(IntegrationClientRegistrar.class)
public class IntegrationAutoConfiguration {
}
