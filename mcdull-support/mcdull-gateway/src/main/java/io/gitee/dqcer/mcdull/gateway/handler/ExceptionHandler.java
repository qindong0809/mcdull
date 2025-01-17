package io.gitee.dqcer.mcdull.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import jakarta.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 异常处理程序
 *
 * @author dqcer
 * @since  2021/12/20
 */
public class ExceptionHandler implements WebExceptionHandler, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Resource
    private ObjectMapper objectMapper;


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        log.error("gateway exception uri: {} message: {} ", uri, ex.getMessage());
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                Result<?> error;
                if (ex instanceof ResponseStatusException) {
                    HttpStatus status = (HttpStatus) ((ResponseStatusException) ex).getStatusCode();
                    error = Result.error(status.value(), status.getReasonPhrase(), ex.getMessage());
                } else {
                    log.warn("网关异常处理: {}", ex.getMessage());
                    error = Result.error(CodeEnum.INTERNAL_SERVER_ERROR, Collections.singletonList(ex.getMessage()));
                }
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(error));
            } catch (JsonProcessingException e) {
                log.error("json 处理异常", e);
                String m = "{\"code\":" + CodeEnum.INTERNAL_SERVER_ERROR + ", \"data\":null, \"message\":\""
                        + CodeEnum.INTERNAL_SERVER_ERROR.getMessage() + "\"}";
                return bufferFactory.wrap(m.getBytes(StandardCharsets.UTF_8));
            }
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
