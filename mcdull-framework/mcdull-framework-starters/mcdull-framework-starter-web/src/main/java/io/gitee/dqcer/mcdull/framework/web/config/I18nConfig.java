package io.gitee.dqcer.mcdull.framework.web.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

/**
 * i18n配置
 *
 * @author dqcer
 * @since 2023/12/27
 */
@Configuration
public class I18nConfig {

    @PostConstruct
    void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames("i18n/framework", "i18n/message");
        // TODO: 2023/12/27 修改路径可读取外部文件
        return messageSource;
    }


}
