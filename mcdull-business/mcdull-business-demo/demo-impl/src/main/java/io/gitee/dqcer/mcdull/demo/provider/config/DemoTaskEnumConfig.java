package io.gitee.dqcer.mcdull.demo.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * demo 实现侧基础设施配置。
 */
@Configuration
public class DemoTaskEnumConfig {

    @Bean
    public DemoTaskTextResolver demoTaskTextResolver() {
        return new DemoTaskTextResolver();
    }
}
