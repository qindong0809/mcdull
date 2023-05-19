package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.OssProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;

/**
 * multipart file config
 *
 * @author dqcer
 * @since 2023/05/19
 */
@Configuration
public class MultipartFileConfig {

    @Resource
    private McdullProperties mcdullProperties;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        OssProperties oss = mcdullProperties.getOss();
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofGigabytes(oss.getMaxFileSize()));
        factory.setMaxRequestSize(DataSize.ofGigabytes(oss.getMaxRequestSize()));
        return factory.createMultipartConfig();
    }
}
