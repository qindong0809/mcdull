package io.gitee.dqcer.mcdull.framework.web.interceptor;

import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.LanguageEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserSession;
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
import java.util.Collections;
import java.util.List;

/**
 * 基础信息拦截器
 *
 * @author dqcer
 * @since 2021/08/19
 */
public abstract class BaseInfoInterceptor implements HandlerInterceptor {

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
                log.debug("Interceptor Un Authorize: {}", requestUrl);
            }
            return true;
        }

        UnifySession unifySession = UserContextHolder.getSession();
        // 获取当前语言环境
        String language = getCurrentLanguage(request);
        unifySession.setLanguage(language);

        if (enableAuth()) {
            String authorization = request.getHeader(HttpHeaderConstants.AUTHORIZATION);
            if (authorization == null || authorization.trim().length() == 0) {
                log.warn("Authentication failed. url: {} Request header parameter '{}' not exist!", requestUrl, HttpHeaderConstants.AUTHORIZATION);
                response.getWriter().write(errorJson(CodeEnum.UN_AUTHORIZATION));
                return false;
            }
            if (!authorization.startsWith(HttpHeaderConstants.BEARER)) {
                log.error("认证失败 头部参数缺失'{}'关键字", HttpHeaderConstants.BEARER);
                response.getWriter().write(errorJson(CodeEnum.UN_AUTHORIZATION));
                return false;
            }
            String token = authorization.substring(HttpHeaderConstants.BEARER.length());
            // token 校验
            Result<UserSession> result = authCheck(token);
            if (!result.isOk()) {
                log.warn("认证失败 result: {}", result);
                response.getWriter().write(errorResult(result));
                return false;
            }
            UserSession userSession = result.getData();
            Long userId = userSession.getUserId();
            unifySession.setUserType(userSession.getType());
            unifySession.setUserId(userId);
        }

        UserContextHolder.setSession(unifySession);

        if (requestUrl.startsWith(GlobalConstant.INNER_API)) {
            return true;
        }

        // 管理人员放过
        if (UserContextHolder.isAdmin()) {
            return true;
        }

        // 资源模块效验
        Authorized authorized = method.getMethodAnnotation(Authorized.class);
        if (null != authorized) {
            String code = authorized.value();
            if (code.trim().length() == 0) {
                return true;
            }

            if (log.isDebugEnabled()) {
                log.debug("Interceptor check power code: {}", code);
            }
            String userPowerCacheKey = MessageFormat.format("framework:web:interceptor:power:{0}", unifySession.getUserId());
            List<UserPowerVO> userPower = cacheChannel.get(userPowerCacheKey, List.class);
            if (ObjUtil.isNull(userPower)) {
                userPower = getUserPower();
                if (ObjUtil.isNull(userPower)) {
                    log.warn("数据库无 userId: {} 对应配置的角色权限", UserContextHolder.currentUserId());
                    response.getWriter().write(errorJson(CodeEnum.POWER_CHECK_MODULE));
                    return false;
                }
                cacheChannel.put(userPowerCacheKey, userPower, 3000);
            }

            boolean anyMatch = userPower.stream().anyMatch(i -> i.getModules().contains(code));
            if (!anyMatch) {
                log.warn("没有对应的模块权限: {}, userPower: {}", CodeEnum.POWER_CHECK_MODULE, userPower);
                response.getWriter().write(errorJson(CodeEnum.POWER_CHECK_MODULE));
                return false;
            }
        }
        return true;
    }

    /**
     * 得到当前的语言
     *
     * @param request 请求
     * @return {@link String}
     */
    private String getCurrentLanguage(HttpServletRequest request) {
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(','));
        }
        return language;
    }

    /**
     * 是否启用token验证
     *
     * @return boolean
     */
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

    /**
     * token 验证
     *
     * @param token 令牌
     * @return {@link Result<Long>}
     */
    protected Result<UserSession> authCheck(String token) {
        return Result.success();
    }


    /**
     * 获取当前用户模块权限
     *
     * @return {@link List<UserPowerVO>}
     */
    protected List<UserPowerVO> getUserPower() {
        return Collections.emptyList();
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
