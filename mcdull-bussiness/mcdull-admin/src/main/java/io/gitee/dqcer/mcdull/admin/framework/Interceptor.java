package io.gitee.dqcer.mcdull.admin.framework;


import io.gitee.dqcer.mcdull.admin.framework.auth.SecurityInterceptor;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.interceptor.DynamicDatasourceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        registry.addInterceptor(dynamicDataSource())
                .addPathPatterns(GlobalConstant.ALL_PATTERNS).excludePathPatterns(GlobalConstant.LOGIN_URL)
                .order(GlobalConstant.Order.INTERCEPTOR_DS);
        registry.addInterceptor(securityInterceptor())
                .addPathPatterns(GlobalConstant.ALL_PATTERNS).excludePathPatterns(GlobalConstant.LOGIN_URL)
                .order(GlobalConstant.Order.INTERCEPTOR_BASE);
    }

    /**
     * 安全拦截器
     *
     * @return {@link HandlerInterceptor}
     */
    @Bean
    public HandlerInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }

    /**
     * 获取动态数据源
     *
     * @return {@link HandlerInterceptor}
     */
    @Bean
    public HandlerInterceptor dynamicDataSource() {
        return new DynamicDatasourceInterceptor();
    }

}
