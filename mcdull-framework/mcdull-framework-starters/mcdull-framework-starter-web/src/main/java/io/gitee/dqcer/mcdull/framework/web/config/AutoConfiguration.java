package io.gitee.dqcer.mcdull.framework.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;
import io.gitee.dqcer.mcdull.framework.spi.RequestDataRouting;
//import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsAspect;
import io.gitee.dqcer.mcdull.framework.web.aspect.TranslatorAspect;
import io.gitee.dqcer.mcdull.framework.spi.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.framework.web.component.impl.DynamicLocaleMessageSourceImpl;
import io.gitee.dqcer.mcdull.framework.web.context.AsyncContextSupport;
import io.gitee.dqcer.mcdull.framework.web.context.MdcContextPropagator;
import io.gitee.dqcer.mcdull.framework.web.context.UserSessionContextPropagator;
import io.gitee.dqcer.mcdull.framework.web.filter.HttpTraceLogFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

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
    public FilterRegistrationBean<HttpTraceLogFilter> traceLogFilterRegistrationBean(ObjectProvider<RequestDataRouting> dataRoutingProvider) {
        FilterRegistrationBean<HttpTraceLogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new HttpTraceLogFilter(dataRoutingProvider.getIfAvailable(() -> RequestDataRouting.NOOP)));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        // 设置记录的请求信息级别
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setIncludeHeaders(false);
        filter.setMaxPayloadLength(1000);
        registrationBean.setFilter(filter);
        return registrationBean;
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

    @Bean
    public DynamicLocaleMessageSource dynamicLocaleMessageSource() {
        return new DynamicLocaleMessageSourceImpl();
    }

    @Bean
    public AsyncContextPropagator mdcContextPropagator() {
        return new MdcContextPropagator();
    }

    @Bean
    public AsyncContextPropagator userSessionContextPropagator() {
        return new UserSessionContextPropagator();
    }

    /**
     * 汇集容器内全部上下文传播器（web 自带的日志/登录态，以及数据源等组件贡献的实现）
     *
     * @return {@link AsyncContextSupport}
     */
    @Bean
    public AsyncContextSupport asyncContextSupport(ObjectProvider<AsyncContextPropagator> propagatorProvider) {
        return new AsyncContextSupport(propagatorProvider.orderedStream().toList());
    }
}
