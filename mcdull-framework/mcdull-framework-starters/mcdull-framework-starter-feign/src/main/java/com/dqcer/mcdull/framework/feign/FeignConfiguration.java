package com.dqcer.mcdull.framework.feign;


import com.dqcer.framework.base.constants.HttpHeaderConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * feign 配置
 *
 * @author dqcer
 * @version 2022/06/12
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignConfiguration.class);

    public FeignConfiguration() {
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String path = requestTemplate.path();
        if (log.isDebugEnabled()) {
            log.debug("Feign RequestInterceptor Path: {}", path);
        }
//        if (path.equals("/feign/token/valid")) {
//            return;
//        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            requestTemplate.header(HttpHeaderConstants.U_ID, request.getHeader(HttpHeaderConstants.U_ID));
            requestTemplate.header(HttpHeaderConstants.T_ID, request.getHeader(HttpHeaderConstants.T_ID));
            requestTemplate.header(HttpHeaderConstants.TRACE_ID_HEADER, request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER));
            return;
        }
        log.warn("RequestContextHolder.getRequestAttributes() is null");
    }

    @Bean
    public feign.Logger.Level loggerLevel() {
        return feign.Logger.Level.BASIC;
    }
}