package io.gitee.dqcer.framework.feign;

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
 * @version 2022/12/25
 */
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
        // 使用原Feign Exception
        log.error("feign 调用异常: {}", request);
        return exception;
    }
}
