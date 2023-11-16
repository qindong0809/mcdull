package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;


import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.mysql.interceptor.DynamicDatasourceInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 *
 * @author dqcer
 * @since 2022/05/10
 */
@Configuration
public class WebInterceptor implements WebMvcConfigurer {


    @Value("${sa-token.token-name}")
    private String tokenName;

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        if (StrUtil.isBlank(tokenName)) {
            registry.addInterceptor(getBaseInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
        }
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
        return new BaseInterceptor();
    }

}
