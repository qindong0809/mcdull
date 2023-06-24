package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;

import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.LanguageEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ResultParse;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.UserService;
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
 * @since 2021/08/19
 */
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BaseInterceptor.class);

    @Resource
    private CacheChannel cacheChannel;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestUrl = request.getRequestURI();
        if (log.isDebugEnabled()) {
            log.debug("BaseInfoInterceptor#preHandle requestURI:[{}]", requestUrl);
        }

        if (! (handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        // 无需认证
        UnAuthorize unauthorize = method.getMethodAnnotation(UnAuthorize.class);
        if (null != unauthorize) {
            if (log.isDebugEnabled()) {
                log.debug("UnAuthorize: {}", requestUrl);
            }
            return true;
        }

        // 设置用户上下文
        UnifySession unifySession = setUserContextHolder(request);

        if (requestUrl.startsWith(GlobalConstant.INNER_API)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // 资源模块权限检查
        return powerCheckModule(response, method, unifySession);
    }

    /**
     * 资源模块权限检查
     *
     * @param response     响应
     * @param method       方法
     * @param unifySession 统一会话
     * @return boolean
     * @throws IOException ioexception
     */
    private boolean powerCheckModule(HttpServletResponse response, HandlerMethod method, UnifySession unifySession) throws IOException {
        Authorized authorized = method.getMethodAnnotation(Authorized.class);
        if (null != authorized) {
            String code = authorized.value();
            if (code != null && code.trim().length() > 0) {
                String userPowerCacheKey = MessageFormat.format("web:interceptor:power:{0}", unifySession.getUserId());
                List<UserPowerVO> userPower = cacheChannel.get(userPowerCacheKey, List.class);
                if (ObjUtil.isNull(userPower)) {
                    userPower = ResultParse.getInstance(userService.queryResourceModules(unifySession.getUserId()));
                    if (log.isDebugEnabled()) {
                        log.debug("userPower: {}", userPower);
                    }
                    cacheChannel.put(userPowerCacheKey, userPower, 3000);
                }
                boolean anyMatch = userPower.stream().anyMatch(i -> i.getModules().contains(code));
                if (!anyMatch) {
                    log.warn("没有对应的模块权限, 模块：{}, userPower: {}", CodeEnum.POWER_CHECK_MODULE, userPower);
                    String json = "{\"code\":"+CodeEnum.POWER_CHECK_MODULE.getCode()+
                            ", \"data\":null, \"message\":\""+CodeEnum.POWER_CHECK_MODULE.getMessage()+"\"}";
                    response.getWriter().write(json);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 设置用户上下文
     *
     * @param request 请求
     * @return {@link UnifySession}
     */
    private static UnifySession setUserContextHolder(HttpServletRequest request) {
        UnifySession unifySession = new UnifySession();
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(','));
        }
        unifySession.setLanguage(language);
        unifySession.setUserId(Long.valueOf(request.getHeader(HttpHeaderConstants.U_ID)));
        String tenantId = request.getHeader(HttpHeaderConstants.T_ID);
        if (StrUtil.isNotBlank(tenantId)) {
            unifySession.setTenantId(Long.valueOf(tenantId));
        }
        unifySession.setTraceId(request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER));
        UserContextHolder.setSession(unifySession);
        return unifySession;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clearSession();
    }
}
