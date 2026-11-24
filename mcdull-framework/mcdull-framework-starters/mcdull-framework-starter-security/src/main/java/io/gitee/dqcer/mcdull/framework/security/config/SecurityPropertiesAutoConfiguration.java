package io.gitee.dqcer.mcdull.framework.security.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 注册 starter-security 自有的配置属性。
 */
@AutoConfiguration
@EnableConfigurationProperties(GatewayProperties.class)
public class SecurityPropertiesAutoConfiguration {
}
