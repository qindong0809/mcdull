package com.dqcer.mcdull.gateway.handler;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
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

import javax.annotation.Resource;
import java.net.URI;

/**
 * 异常处理程序
 *
 * @author dqcer
 * @version  2021/12/20
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


        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                Result<?> error = null;
                if (ex instanceof ResponseStatusException) {
                    HttpStatus status = ((ResponseStatusException) ex).getStatus();
                    switch (status) {
                        case NOT_FOUND:
                            // 404
                            error = Result.error(ResultCode.NOT_FOUND);
                            break;
                        case METHOD_NOT_ALLOWED:
                            // 405
                            error = Result.error(ResultCode.METHOD_NOT_ALLOWED);
                            break;
                        case BAD_REQUEST:
                            // 参数异常
                            log.warn("网关异常处理", ex);
                            error = Result.error(ResultCode.ERROR_PARAMETERS);
                            break;
                        default:
                            // 其他异常默认为500
                            log.warn("网关异常处理", ex);
                            error = Result.error(ResultCode.ERROR_UNKNOWN);
                            break;
                    }
                } else {
                    log.warn("网关异常处理: {}", ex.getMessage());
                    error = Result.error(ResultCode.ERROR_UNKNOWN);
                }
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(error));
            } catch (JsonProcessingException e) {
                log.error("json 处理异常", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
