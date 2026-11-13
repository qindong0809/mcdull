package io.gitee.dqcer.mcdull.focus.config;

import io.gitee.dqcer.mcdull.framework.security.AbstractWebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebWebMvcConfigurer extends AbstractWebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mp3/**")
            .addResourceLocations("classpath:/mp3/")
            .resourceChain(true);
        super.addResourceHandlers(registry);
    }
}
