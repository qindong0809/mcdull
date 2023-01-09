package com.dqcer.mcdull.framework.web.config;

import com.dqcer.mcdull.framework.web.aspect.OperationLogsAspect;
import com.dqcer.mcdull.framework.web.aspect.TranslatorAspect;
import com.dqcer.mcdull.framework.web.feign.service.LogFeignClient;
import com.dqcer.mcdull.framework.web.filter.HttpTraceLogFilter;
import com.dqcer.mcdull.framework.web.listener.LogEventListener;
import com.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 自动配置
 *
 * @author dqcer
 * @version  22:21 2021/4/28
 */
@ConditionalOnWebApplication
@Configuration
public class AutoConfiguration {


//    @Bean
//    public LogEventListener logListener() {
//        return new LogEventListener();
//    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

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

//    @Bean
//    public OperationLogsAspect operationLogsAspect() {
//        return new OperationLogsAspect();
//    }

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
}
