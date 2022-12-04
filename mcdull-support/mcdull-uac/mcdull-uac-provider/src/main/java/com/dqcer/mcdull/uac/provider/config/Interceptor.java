package com.dqcer.mcdull.uac.provider.config;


import com.dqcer.mcdull.framework.mysql.interceptor.DynamicDatasourceInterceptor;
import com.dqcer.mcdull.framework.web.interceptor.BaseInfoInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 *
 * @author dqcer
 * @version 2022/05/10
 */
@Configuration
public class Interceptor implements WebMvcConfigurer {

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getBaseInterceptor()).addPathPatterns("/**").excludePathPatterns("/feign/token/valid", "/login");
    }

    /**
     * 获取动态数据源
     *
     * @return {@link HandlerInterceptor}
     */
    @Bean
    public HandlerInterceptor getDynamicDataSource() {
        return new DynamicDatasourceInterceptor();
    }

    /**
     * 获取安全拦截器
     *
     * @return {@link HandlerInterceptor}
     */
    @Bean
    public HandlerInterceptor getBaseInterceptor() {
        return new BaseInfoInterceptor();
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

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
//        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
//        //序列换成json时,将所有的long变成string
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        list.add(jsonConverter);
//    }

    /**
     * 配置消息转换器
     *
     * @param converters 转换器
     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
//        FastJsonConfig config = new FastJsonConfig();
//
//        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
//        serializeConfig.put(Long.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        config.setSerializeConfig(serializeConfig);
//
//        config.setSerializerFeatures(
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullStringAsEmpty,
//                SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteNullBooleanAsFalse,
//                SerializerFeature.DisableCircularReferenceDetect);
//
//        converter.setFastJsonConfig(config);
//        converter.setDefaultCharset(StandardCharsets.UTF_8);
//        List<MediaType> mediaTypeList = new ArrayList<>();
//        mediaTypeList.add(MediaType.APPLICATION_JSON);
//        converter.setSupportedMediaTypes(mediaTypeList);
//        converters.add(converter);
//    }
}
