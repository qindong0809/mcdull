package io.gitee.dqcer.mcdull.gateway.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * 过滤器
 *
 * @author dqcer
 * @since 2022/10/27
 */
public abstract class AbstractFilter {


    /**
     * 添加头部
     *
     * @param mutate 变异
     * @param name   的名字
     * @param value  值
     */
    protected void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        mutate.header(name, valueStr);
    }

    /**
     * 错误响应
     *
     * @param response 响应
     * @param code     代码
     * @param msg      味精
     * @return {@link Mono<Void>}
     */
    protected Mono<Void> errorResponse(ServerHttpResponse response, int code, String msg) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatusCode(HttpStatus.OK);
        String m = "{\"code\":" + code + ", \"data\":null, \"msg\":\"" + msg + "\"}";
        DataBuffer dataBuffer = response.bufferFactory().wrap(m.getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }


    /**
     * 是否为数字
     *
     * @param str str
     * @return boolean
     */
    protected boolean isNumber(String str) {
        return str.matches("^[0-9]*$");
    }
}
