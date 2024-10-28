package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
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
public class I18nConfig implements SmartInitializingSingleton {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @PostConstruct
    void setDefaultTimeZone() {
        final String timeZone = "UTC";
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        LogHelp.info(log, "setDefaultTimeZone: {}", timeZone);

    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames("classpath:i18n/framework", "classpath:i18n/message");
        messageSource.setCacheSeconds(-1);
        // TODO: 2023/12/27 修改路径可读取外部文件
        return messageSource;
    }


    @Override
    public void afterSingletonsInstantiated() {
        LogHelp.info(log, "i18nConfig i18n file loading...");
        this.reloadI18nFile();
    }

    private void reloadI18nFile() {
        // todo
    }
}
