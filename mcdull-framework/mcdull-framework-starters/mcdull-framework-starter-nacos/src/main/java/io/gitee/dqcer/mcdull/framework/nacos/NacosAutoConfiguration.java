package io.gitee.dqcer.mcdull.framework.nacos;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * nacos 自动配置。
 * <p>
 * 业务服务仅将 Nacos 作为注册中心，不加载配置中心的日志级别监听器（LogLevelListener）。
 *
 * @author dqcer
 * @since 1.0.0
 */
@AutoConfiguration
@Import({PrometheusConfiguration.class, ShutdownDiscovery.class})
public class NacosAutoConfiguration {
}
