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
import com.dqcer.framework.base.wrapper.ICode;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());

        if (!httpStatus.equals(HttpStatus.OK)) {
            // 404 会重定向到 /error
            log.error("http 请求异常 requestUrl: {}, httpStatus: {} ", requestUrl, httpStatus);
            response.getWriter().write(errorJson(httpStatus));
            return false;
        }

        if (!(handler instanceof HandlerMethod)) {
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

        UnifySession unifySession = UserContextHolder.getSession();
        // 获取当前语言环境
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(','));
        }
        unifySession.setLanguage(language);


        if (enableAuth()) {
            String authorization = request.getHeader(HttpHeaderConstants.AUTHORIZATION);
            if (authorization == null || authorization.trim().length() == 0) {
                log.warn("认证失败 头部参数'authorization'缺失 url: {}", requestUrl);
                response.getWriter().write(errorJson(CodeEnum.UN_AUTHORIZATION));
                return false;
            }

            String token = authorization.substring(HttpHeaderConstants.BEARER.length());
            if (token.trim().length() == 0) {
                log.warn("认证失败 头部参数缺失缺失'Bearer '");
                response.getWriter().write(errorJson(CodeEnum.UN_AUTHORIZATION));
                return false;
            }

            Result<Long> result = authCheck(token);
            if (!result.isOk()) {
                log.warn("认证失败 result: {}", result);
                response.getWriter().write(errorResult(result));
                return false;
            }
            Long userId = result.getData();
            unifySession.setUserId(userId);
        }

        UserContextHolder.setSession(unifySession);

        if (requestUrl.startsWith(GlobalConstant.INNER_API)) {
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
                    response.getWriter().write(errorJson(CodeEnum.POWER_CHECK_MODULE));
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean enableAuth() {
        return false;
    }

    private String errorJson(ICode codeEnum) {
        return "{\"code\":"+ codeEnum.getCode() +
                        ", \"data\":null, \"message\":\"" + codeEnum.getMessage() + "\"}";
    }

    private String errorJson(HttpStatus codeEnum) {
        return "{\"code\":"+ codeEnum.value() +
                ", \"data\":null, \"message\":\"" + codeEnum.getReasonPhrase() + "\"}";
    }

    private String errorResult(Result<?> result) {
        return "{\"code\":"+ result.getCode() +
                ", \"data\":" + result.getData() + ", \"message\":\"" + result.getMessage() + "\"}";
    }

    protected Result<Long> authCheck(String token) {
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
