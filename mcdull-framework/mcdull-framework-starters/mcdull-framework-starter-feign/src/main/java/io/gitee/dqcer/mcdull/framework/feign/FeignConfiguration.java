package io.gitee.dqcer.mcdull.framework.feign;

import cn.hutool.core.util.StrUtil;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign 自动装配与全局配置
 *
 * @author dqcer
 * @since 2022/06/12
 */
@AutoConfiguration
@Import(FeignExceptionAdvice.class)
public class FeignConfiguration implements RequestInterceptor {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FeignConfiguration.class);

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (log.isDebugEnabled()) {
            log.debug("Feign RequestInterceptor Path: {}", requestTemplate.path());
        }

        // 1. 优先从 UserContextHolder 获取会话（兼容异步线程池、定时任务等无 Web Request 场景）
        UnifySession session = UserContextHolder.getSession();
        if (session != null) {
            setHeaderIfNotBlank(requestTemplate, HttpHeaderConstants.U_ID, session.getUserId());
            if (session.getTenantId() != null) {
                setHeaderIfNotBlank(requestTemplate, HttpHeaderConstants.T_ID, String.valueOf(session.getTenantId()));
            }
            setHeaderIfNotBlank(requestTemplate, HttpHeaderConstants.TRACE_ID_HEADER, session.getTraceId());
            return;
        }

        // 2. 兜底从当前 Web 请求头透传（适用于未经过登录拦截器的内部透传场景）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            setHeaderIfNotBlank(requestTemplate, HttpHeaderConstants.U_ID, request.getHeader(HttpHeaderConstants.U_ID));
            setHeaderIfNotBlank(requestTemplate, HttpHeaderConstants.T_ID, request.getHeader(HttpHeaderConstants.T_ID));
            setHeaderIfNotBlank(requestTemplate, HttpHeaderConstants.TRACE_ID_HEADER, request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER));
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Feign 调用无上下文会话信息, path: {}", requestTemplate.path());
        }
    }

    private void setHeaderIfNotBlank(RequestTemplate template, String name, String value) {
        if (StrUtil.isNotBlank(value)) {
            template.header(name, value);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * 按环境动态控制日志级别：非生产环境打印 BASIC（请求方法、URL、耗时），生产环境仅记录 NONE
     */
    @Bean
    @ConditionalOnMissingBean
    public Logger.Level loggerLevel() {
        if ("prod".equalsIgnoreCase(activeProfile)) {
            return Logger.Level.NONE;
        }
        return Logger.Level.BASIC;
    }
}
