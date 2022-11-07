package com.dqcer.mcdull.framework.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.dqcer.framework.base.auth.CacheUser;
import com.dqcer.framework.base.auth.UnifySession;
import com.dqcer.framework.base.auth.UserStorage;
import com.dqcer.framework.base.constants.CacheConstant;
import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.constants.SysConstants;
import com.dqcer.framework.base.utils.ObjUtil;
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
import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * 基础信息拦截器
 *
 * @author dqcer
 * @date 2021/08/19
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

        if (requestURI.startsWith(SysConstants.FEIGN_URL)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HandlerMethod method = (HandlerMethod) handler;

        // 获取当前用户信息
        UnifySession unifySession = new UnifySession();
        unifySession.setLanguage(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
        unifySession.setIp("暂时空着");

        UnAuthorize unauthorize = method.getMethodAnnotation(UnAuthorize.class);
        if (null != unauthorize) {
            if (log.isDebugEnabled()) {
                log.debug("UnAuthorize: {}", requestURI);
            }
            UserStorage.setSession(unifySession);
            return true;
        }

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

        Object obj = redisClient.get(MessageFormat.format(CacheConstant.SSO_TOKEN, token));

        if (ObjUtil.isNull(obj)) {
            log.warn("BaseInfoInterceptor:  7天强制过期下线");
            response.getWriter().write(JSON.toJSONString(Result.error(ResultCode.UN_AUTHORIZATION)));
            return false;
        }

        CacheUser user = (CacheUser) obj;

        if (log.isDebugEnabled()) {
            log.debug("CacheUser:{}", user);
        }

        if (CacheUser.OFFLINE.equals(user.getOnlineStatus())) {
            log.warn("BaseInfoInterceptor:  异地登录");
            response.getWriter().write(JSON.toJSONString(Result.error(ResultCode.UN_AUTHORIZATION)));
            //  异地登录
            return false;
        }

        LocalDateTime lastActiveTime = user.getLastActiveTime();


        LocalDateTime now = LocalDateTime.now();
        if (lastActiveTime.plusMinutes(30).isBefore(now)) {
            log.warn("BaseInfoInterceptor:  用户操作已过期");
            response.getWriter().write(JSON.toJSONString(Result.error(ResultCode.UN_AUTHORIZATION)));
            return false;
        }


        redisClient.set(MessageFormat.format(CacheConstant.SSO_TOKEN, token), user.setLastActiveTime(now));

        unifySession.setAccountId(user.getAccountId());
        unifySession.setTenantId(user.getTenantId());
        UserStorage.setSession(unifySession);

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserStorage.clearSession();
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
