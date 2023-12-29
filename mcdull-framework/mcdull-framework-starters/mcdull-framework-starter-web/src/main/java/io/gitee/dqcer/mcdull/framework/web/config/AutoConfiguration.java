package io.gitee.dqcer.mcdull.framework.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsAspect;
import io.gitee.dqcer.mcdull.framework.web.aspect.TranslatorAspect;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.framework.web.component.impl.DynamicLocaleMessageSourceImpl;
import io.gitee.dqcer.mcdull.framework.web.filter.HttpTraceLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 自动配置
 *
 * @author dqcer
 * @since  22:21 2021/4/28
 */
//@ConditionalOnWebApplication
@Configuration
public class AutoConfiguration {

    /**
     * 跟踪日志过滤器bean注册
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<HttpTraceLogFilter> traceLogFilterRegistrationBean() {
        FilterRegistrationBean<HttpTraceLogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new HttpTraceLogFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public TranslatorAspect translatorAspect() {
        return new TranslatorAspect();
    }

    @Bean
    public OperationLogsAspect operationLogsAspect() {
        return new OperationLogsAspect();
    }

    /**
     * 添加Long转json精度丢失的配置
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public DynamicLocaleMessageSource dynamicLocaleMessageSource() {
        return new DynamicLocaleMessageSourceImpl();
    }
}
