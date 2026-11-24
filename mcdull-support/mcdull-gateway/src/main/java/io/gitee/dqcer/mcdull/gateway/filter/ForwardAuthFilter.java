package io.gitee.dqcer.mcdull.gateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器，为请求添加 Same-Token
 *
 * @author dqcer
 * @since 2023/7/27
 */
@Component
public class ForwardAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String sameToken = SaSameUtil.getToken();
        ServerHttpRequest newRequest = exchange
            .getRequest()
            .mutate()
            // 覆盖客户端传入的 Same-Token，防止伪造内部信任凭证
            .headers(headers -> headers.set(SaSameUtil.SAME_TOKEN, sameToken))
            .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }
}
