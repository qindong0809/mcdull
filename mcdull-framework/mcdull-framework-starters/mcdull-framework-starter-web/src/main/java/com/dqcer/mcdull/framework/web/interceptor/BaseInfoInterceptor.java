package com.dqcer.mcdull.framework.web.interceptor;

import com.dqcer.framework.base.annotation.Authorized;
import com.dqcer.framework.base.annotation.UnAuthorize;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.enums.LanguageEnum;
import com.dqcer.framework.base.storage.UnifySession;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.framework.base.wrapper.CodeEnum;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
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
import java.util.List;

/**
 * 基础信息拦截器
 *
 * @author dqcer
 * @version 2021/08/19
 */
public class BaseInfoInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BaseInfoInterceptor.class);

    @Resource
    private CacheChannel cacheChannel;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestUrl = request.getRequestURI();
        if (log.isDebugEnabled()) {
            log.debug("Interceptor url:{}", requestUrl);
        }

        if (! (handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        // 无需认证
        UnAuthorize unauthorize = method.getMethodAnnotation(UnAuthorize.class);
        if (null != unauthorize) {
            if (log.isDebugEnabled()) {
                log.debug("Un Authorize: {}", requestUrl);
            }
            return true;
        }

        // 获取当前语言环境
        UnifySession unifySession = UserContextHolder.getSession();
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(','));
        }
        unifySession.setLanguage(language);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if (enableAuth()) {
            String authorization = request.getHeader(HttpHeaderConstants.AUTHORIZATION);
            if (authorization == null || authorization.trim().length() == 0) {
                log.warn("认证失败 头部参数'authorization'缺失 url: {}", requestUrl);
                response.getWriter().write(authErrorJson());
                return false;
            }

            String token = authorization.substring(HttpHeaderConstants.BEARER.length());
            if (token.trim().length() == 0) {
                log.warn("认证失败 头部参数缺失缺失'Bearer '");
                response.getWriter().write(authErrorJson());
                return false;
            }

            Result<?> result = authCheck(token, unifySession);
            if (!result.isOk()) {
                log.warn("认证失败 result: {}", result);
                response.getWriter().write(authErrorJson());
                return false;
            }
        }

        UserContextHolder.setSession(unifySession);

        if (requestUrl.startsWith(GlobalConstant.INTERIOR_API)) {
            return true;
        }

        // 资源模块效验
        Authorized authorized = method.getMethodAnnotation(Authorized.class);
        if (null != authorized) {
            String code = authorized.value();
            if (code.trim().length() > 0) {
                String userPowerCacheKey = MessageFormat.format("framework:web:interceptor:power:{0}", unifySession.getUserId());
                List<UserPowerVO> userPower = cacheChannel.get(userPowerCacheKey, List.class);
                if (ObjUtil.isNull(userPower)) {
                    userPower = getUserPower();
                    cacheChannel.put(userPowerCacheKey, userPower, 3000);
                }
                boolean anyMatch = userPower.stream().anyMatch(i -> i.getModules().contains(code));
                if (!anyMatch) {
                    log.warn("没有对应的模块权限: {}, userPower: {}", CodeEnum.POWER_CHECK_MODULE, userPower);
                    String json = "{\"code\":"+CodeEnum.POWER_CHECK_MODULE.getCode()+
                                    ", \"data\":null, \"message\":\""+CodeEnum.POWER_CHECK_MODULE.getMessage()+"\"}";
                    response.getWriter().write(json);
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean enableAuth() {
        return false;
    }

    private String authErrorJson() {
        return "{\"code\":"+ CodeEnum.UN_AUTHORIZATION.getCode() +
                        ", \"data\":null, \"message\":\""+CodeEnum.UN_AUTHORIZATION.getMessage()+"\"}";
    }

    protected Result<?> authCheck(String token, UnifySession unifySession) {
        return Result.ok();
    }


    protected List<UserPowerVO> getUserPower() {
//        return FeignResultParse.getInstance(powerCheckFeignClient.queryResourceModules());
        return null;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
