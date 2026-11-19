package io.gitee.dqcer.mcdull.framework.tag.config;

import feign.RequestInterceptor;
import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextPropagator;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.WebFilter;

import java.io.IOException;


@AutoConfiguration
@EnableConfigurationProperties(TagProperties.class)
@ConditionalOnProperty(prefix = "mcdull.tag", name = "enabled", havingValue = "true", matchIfMissing = true)
@LoadBalancerClients(defaultConfiguration = TagLoadBalancerConfig.class)
public class TagAutoConfiguration {

    /**
     * 染色标签的跨线程传播器。
     * <p>
     * 收集方是 starter-web 的 {@code AsyncContextSupport}（按 {@link AsyncContextPropagator} bean 汇集），
     * 因此只有同时引入 starter-web 的应用才会真正传递；纯 WebFlux 网关没有该线程池，注册了也不起作用。
     */
    @Bean
    public AsyncContextPropagator envTagContextPropagator() {
        return new EnvTagContextPropagator();
    }

    /**
     * 仅在 WebFlux 网关中生效：生产环境自动清洗外部传入的 X-Env-Tag，防止恶意伪造染色标签
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(WebFilter.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    static class ReactiveTagCleanConfig {

        @Bean
        @ConditionalOnProperty(prefix = "mcdull.tag", name = "clean-header", havingValue = "true")
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public WebFilter prodEnvTagCleanFilter() {
            return (exchange, chain) -> {
                if (exchange.getRequest().getHeaders().containsKey(EnvTagContextHolder.HEADER_ENV_TAG)) {
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                        .mutate()
                        .headers(headers -> headers.remove(EnvTagContextHolder.HEADER_ENV_TAG))
                        .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }
                return chain.filter(exchange);
            };
        }
    }

    /**
     * 仅在 Servlet 业务微服务中生效（双重条件防护，防止 WebFlux 网关扫描时类缺失报错）
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({OncePerRequestFilter.class, HttpServletRequest.class})
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletTagConfig {

        @Bean
        public FilterRegistrationBean<OncePerRequestFilter> envTagServletFilter() {
            OncePerRequestFilter filter = new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                    throws ServletException, IOException {
                    try {
                        String tag = request.getHeader(EnvTagContextHolder.HEADER_ENV_TAG);
                        if (StringUtils.hasText(tag)) {
                            EnvTagContextHolder.setTag(tag);
                        }
                        chain.doFilter(request, response);
                    } finally {
                        EnvTagContextHolder.clear();
                    }
                }
            };
            FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>(filter);
            registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
            return registration;
        }
    }

    /**
     * 仅在引入了 OpenFeign 的微服务中生效
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RequestInterceptor.class)
    static class FeignTagConfig {

        @Bean
        public RequestInterceptor envTagFeignInterceptor() {
            return template -> {
                String tag = EnvTagContextHolder.getTag();
                if (StringUtils.hasText(tag)) {
                    template.header(EnvTagContextHolder.HEADER_ENV_TAG, tag);
                }
            };
        }
    }
}
