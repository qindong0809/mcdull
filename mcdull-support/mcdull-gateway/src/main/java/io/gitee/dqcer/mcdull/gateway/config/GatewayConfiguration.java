package io.gitee.dqcer.mcdull.gateway.config;


import io.gitee.dqcer.mcdull.gateway.handler.ExceptionHandler;
import io.gitee.dqcer.mcdull.gateway.properties.McdullGatewayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


/**
 * 网关配置
 *
 * @author dqcer
 * @since 2022/10/26
 */
@Configuration
@EnableConfigurationProperties(McdullGatewayProperties.class)
public class GatewayConfiguration {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfiguration.class);

    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return new ExceptionHandler();
    }

    /**
     * 跨域处理
     *
     * 注：仅开发环境使用，测试、生产环境使用nginx处理跨域
     *
     * @return {@link CorsWebFilter}
     */
    @Bean
    @Profile("dev")
    public CorsWebFilter corsWebFilter() {
        log.info("CorsWebFilter ...");
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        List<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add(CorsConfiguration.ALL);
        configuration.setAllowedOriginPatterns(allowedOriginPatterns);

        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.addAllowedMethod(CorsConfiguration.ALL);

        configurationSource.registerCorsConfiguration("/**", configuration);
        return new CorsWebFilter(configurationSource);
    }

    /**
     * 租户限流
     *
     * @return {@link KeyResolver}
     */
    @Bean
    public KeyResolver tenantKeyResolver() {
        return exchange -> {
            ServerHttpRequest request = exchange.getRequest();
            String tenantId = request.getHeaders().getFirst("tenant_id");
            return Mono.just(tenantId);
        };
    }

    /**
     * ip限流 config
     *
     * @return {@link KeyResolver}
     */
    @Primary
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * api接口限流
     *
     * @return {@link KeyResolver}
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
}
