package io.gitee.dqcer.mcdull.frameowrk.soagov;

import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

/**
 * Prometheus配置
 *
 * @author dqcer
 * @since 2022/10/09
 */
@Configuration
@ConditionalOnClass({MeterRegistry.class})
public class PrometheusConfiguration implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(PrometheusConfiguration.class);

	@Resource
	private McdullProperties mcdullProperties;

	@Bean
	MeterRegistryCustomizer<MeterRegistry> appMetricsCommonTags() {
		return registry -> registry.config().commonTags("application", mcdullProperties.getApplicationName());
	}

	@Override
	public void run(String... args) {
		log.info("Prometheus startup successfully! ");
	}
}
