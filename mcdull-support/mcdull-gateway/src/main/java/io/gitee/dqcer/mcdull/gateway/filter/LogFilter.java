package io.gitee.dqcer.mcdull.gateway.filter;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.gateway.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 日志过滤器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Component
public class LogFilter extends AbstractFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        // 浏览器传traceId,暂不进行强制限制
        String traceId = RandomUtil.uuid();
        addHeader(mutate, HttpHeaderConstants.TRACE_ID_HEADER, traceId);
        MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);

        if (log.isDebugEnabled()) {
            log.info("请求地址:{} {} 来源Ip: {}", request.getMethodValue(), request.getURI(), IpUtils.getRealIp(request));
        }

        // 打印请求头
        HttpHeaders headers = exchange.getRequest().getHeaders();
        StringJoiner keyValue = new StringJoiner(StrUtil.COMMA + StrUtil.SPACE);
        headers.forEach((headerName, headerValue) -> {
            keyValue.add(headerName + StrUtil.COLON + String.join(",", headerValue));
        });

        log.info("===Headers===  {}", keyValue);

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            long executeTime = 0L;
            if (startTime != null) {
                executeTime = (System.currentTimeMillis() - startTime);
            }
            // 打印执行时间
            log.info("耗时: {} ms", executeTime);
            MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
        }));
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
