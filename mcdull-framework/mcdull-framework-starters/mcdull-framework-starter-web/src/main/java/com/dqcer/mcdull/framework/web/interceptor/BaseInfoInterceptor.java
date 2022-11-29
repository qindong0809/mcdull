package com.dqcer.mcdull.framework.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.dqcer.framework.base.auth.UnifySession;
import com.dqcer.framework.base.auth.UserContextHolder;
import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.constants.SysConstants;
import com.dqcer.framework.base.enums.LanguageEnum;
import com.dqcer.framework.base.utils.StrUtil;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.framework.redis.operation.RedisClient;
import com.dqcer.mcdull.framework.web.annotation.UnAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 基础信息拦截器
 *
 * @author dqcer
 * @version 2021/08/19
 */
public class BaseInfoInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BaseInfoInterceptor.class);

    @Resource
    private RedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        if (log.isDebugEnabled()) {
            log.debug("BaseInfoInterceptor#preHandle requestURI:[{}]", requestURI);
        }

        // 无需认证
        HandlerMethod method = (HandlerMethod) handler;
        UnAuthorize unauthorize = method.getMethodAnnotation(UnAuthorize.class);
        if (null != unauthorize) {
            if (log.isDebugEnabled()) {
                log.debug("UnAuthorize: {}", requestURI);
            }
            return true;
        }

        // 获取当前用户信息
        UnifySession unifySession = new UnifySession();
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(","));
        }
        unifySession.setLanguage(language);
        unifySession.setUserId(Long.valueOf(request.getHeader(HttpHeaderConstants.U_ID)));
        String tenantId = request.getHeader(HttpHeaderConstants.T_ID);
        if (StrUtil.isNotBlank(tenantId)) {
            unifySession.setTenantId(Long.valueOf(tenantId));
        }
        unifySession.setTraceId(request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER));
        UserContextHolder.setSession(unifySession);

        if (requestURI.startsWith(SysConstants.FEIGN_URL)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // TODO: 2022/11/7 资源模块效验


        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (log.isDebugEnabled()) {
            log.debug("Authorization: {}", authorization);
        }

        if (StrUtil.isBlank(authorization) || !authorization.startsWith(HttpHeaderConstants.BEARER)) {

            response.getWriter().write(JSON.toJSONString(Result.error(ResultCode.UN_AUTHORIZATION)));
            //  认证失败
            return false;
        }

        String token = authorization.substring(HttpHeaderConstants.BEARER.length());




        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clearSession();
    }

    /**
     * 是否执行
     *
     * @param object 对象
     * @return boolean
     */
    public boolean isExecute(Object object) {
        if (object instanceof HttpServletRequest) {
            return false;
        }
        return !(object instanceof HttpServletResponse);
    }
}
