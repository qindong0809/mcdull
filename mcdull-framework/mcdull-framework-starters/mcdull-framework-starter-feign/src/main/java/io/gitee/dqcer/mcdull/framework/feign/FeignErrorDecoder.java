package io.gitee.dqcer.mcdull.framework.feign;

import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * feign错误拦截
 *
 * @author dqcer
 * @since 2022/12/25
 */
public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);

    /**
     * 解码
     *
     * @param methodKey 方法关键
     * @param response  响应
     * @return {@link Exception}
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = FeignException.errorStatus(methodKey, response);
        Request request = exception.request();
        // 仅打印请求方法、URL 和状态码，避免直接打印 Request 对象泄露敏感 Header
        log.error("Feign HTTP 协议异常, status: {}, method: {}, url: {}, response: {}", response.status(), request.httpMethod(), request.url(), exception.contentUTF8());
        return exception;
    }
}
