package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;


import io.gitee.dqcer.mcdull.framework.security.AbstractWebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * 拦截器
 *
 * @author dqcer
 * @since 2022/05/10
 */
@Configuration
public class WebWebMvcConfigurer extends AbstractWebMvcConfigurer {


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
//        registry.addInterceptor(new BaseInterceptor())
//                .addPathPatterns(GlobalConstant.ALL_PATTERNS)
//                .excludePathPatterns(EXCLUDE_PATTERNS);
//    }
}
