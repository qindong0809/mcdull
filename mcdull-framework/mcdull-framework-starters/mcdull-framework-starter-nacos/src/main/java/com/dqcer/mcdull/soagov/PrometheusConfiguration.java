package com.dqcer.mcdull.soagov;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus配置
 *
 * @author dqcer
 * @version 2022/10/09
 */
@Configuration
@ConditionalOnClass({MeterRegistry.class})
public class PrometheusConfiguration implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(PrometheusConfiguration.class);

	@Value("${spring.application.name:unknown}")
	private String applicationName;

	@Bean
	MeterRegistryCustomizer<MeterRegistry> appMetricsCommonTags() {
		return registry -> registry.config().commonTags("application", applicationName);
	}

	@Override
	public void run(String... args) {
		log.info("Prometheus startup successfully! ");
	}
}
