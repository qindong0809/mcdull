package com.dqcer.mcdull.uac.provider.config;

import com.dqcer.framework.base.annotation.Authorized;
import com.dqcer.framework.base.annotation.UnAuthorize;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.enums.LanguageEnum;
import com.dqcer.framework.base.storage.UnifySession;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.framework.base.util.StrUtil;
import com.dqcer.framework.base.wrapper.FeignResultParse;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.uac.provider.web.service.UserService;
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
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BaseInterceptor.class);

    @Resource
    private CacheChannel cacheChannel;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        if (log.isDebugEnabled()) {
            log.debug("BaseInfoInterceptor#preHandle requestURI:[{}]", requestURI);
        }

        if (! (handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        // 无需认证
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

        if (requestURI.startsWith(GlobalConstant.FEIGN_PREFIX)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // 资源模块效验
        Authorized authorized = method.getMethodAnnotation(Authorized.class);
        if (null != authorized) {
            String code = authorized.value();
            if (StrUtil.isNotBlank(code)) {
                String userPowerCacheKey = MessageFormat.format("web:interceptor:power:{0}", unifySession.getUserId());
                List<UserPowerVO> userPower = cacheChannel.get(userPowerCacheKey, List.class);
                if (ObjUtil.isNull(userPower)) {
                    userPower = FeignResultParse.getInstance(userService.queryResourceModules(unifySession.getUserId()));
                    cacheChannel.put(userPowerCacheKey, userPower, 3000);
                }
                boolean anyMatch = userPower.stream().anyMatch(i -> i.getModules().contains(code));
                if (!anyMatch) {
                    log.warn("{}模块权限检查未通过, code: {} userPower: {}", ResultCode.POWER_CHECK_MODULE, code, userPower);
                    response.getWriter().write("{\"code\":999410, \"data\":null, \"msg\":\"无权限\"}");
                    return false;
                }
            }
        }
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