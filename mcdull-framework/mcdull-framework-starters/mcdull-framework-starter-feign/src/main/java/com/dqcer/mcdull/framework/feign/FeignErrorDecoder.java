package com.dqcer.mcdull.framework.feign;

import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
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
        log.error("feign 调用异常: {}", request);
        return exception;
    }
}
