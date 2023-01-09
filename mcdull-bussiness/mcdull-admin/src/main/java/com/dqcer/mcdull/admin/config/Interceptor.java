package com.dqcer.mcdull.admin.config;


import com.dqcer.mcdull.framework.mysql.interceptor.DynamicDatasourceInterceptor;
import com.dqcer.mcdull.framework.web.interceptor.BaseInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器
 *
 * @author dqcer
 * @version 2022/05/10
 */
@Configuration
public class Interceptor implements WebMvcConfigurer {

    @Resource
    private SimpleBaseInfoInterceptor baseInfoInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(baseInfoInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
        registry.addInterceptor(getDynamicDataSource()).addPathPatterns("/**").excludePathPatterns("/login");
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

}
