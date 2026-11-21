package io.gitee.dqcer.mcdull.system.provider.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    /**
     * 单体项目配置（local 环境不启用）
     *
     * local 下前端统一走网关，CORS 由网关 CorsWebFilter 处理；若此处再注册会与网关重复，
     * 导致响应出现两个 Access-Control-Allow-Origin 头被浏览器拦截。
     * 其他环境（dev / test / prod）网关不加 CORS 头，本过滤器保持启用。
     *
     * 注：必须以最高优先级注册，先于业务过滤器（如HttpTraceLogFilter）执行，
     * 否则token失效等场景下业务过滤器直接写出的异常响应将没有CORS头，导致浏览器拦截
     */
    @Bean
    @Profile("!local")
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }
}
