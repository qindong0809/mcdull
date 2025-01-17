package io.gitee.dqcer.mcdull.framework.web.aspect;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import io.gitee.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 操作日志
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Aspect
@Order(GlobalConstant.Order.ASPECT_OPERATION_LOG)
public class OperationLogsAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogsAspect.class);

    @Value("${log.enable:true}")
    private Boolean logEnable;

    /**
     * 操作日志拦截 ..* 表示任意包或子包
     */
    @Pointcut("execution(* io.gitee..*.controller..*.*(..))")
    public void operationLogsCut() {
        // 切点
    }

    @Around("operationLogsCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!logEnable) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = ServletUtil.getRequest();
        String requestUrl = request.getRequestURI();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(UnAuthorize.class) || this.ignoreFilter(requestUrl, PATH_LIST)) {
            return joinPoint.proceed();
        }
        OperationLogsService bean = SpringContextHolder.getBean(OperationLogsService.class);
        if (!bean.needInterceptor(request, method)) {
            return joinPoint.proceed();
        }
        UnifySession<?> unifySession = UserContextHolder.getSession();
        if (unifySession != null) {
            LogHelp.info(log, "Operation Logs Url:{}", unifySession.getRequestUrl());
        }

        long startTime = System.currentTimeMillis();
        boolean successFlag = true;
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            successFlag = false;
            throw e;
        } finally {
            Object[] args = joinPoint.getArgs();
            LogOperationDTO entity = buildLog(request, args, startTime);
            entity.setSuccessFlag(successFlag);
            LogHelp.debug(log, "Operation log dto: {}", entity);
            bean.saveLog(entity, method);
        }
    }


    /**
     * 构建日志
     *
     * @param request   request
     * @param args      参数集
     * @param startTime 开始时间
     * @return {@link LogOperationDTO}
     */
    private LogOperationDTO buildLog(HttpServletRequest request, Object[] args, long startTime) {

        Map<String, String> headers = new HashMap<>(16);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        List<Object> params = new ArrayList<>();
        for (Object arg : args) {
            if (ObjUtil.isNull(arg)) {
                continue;
            }
            if (arg instanceof HttpServletRequest) {
                continue;
            }
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            if (arg instanceof InputStreamSource) {
                continue;
            }
            if (arg instanceof ModelAndView || arg instanceof BindResult) {
                continue;
            }
            params.add(arg);
        }

        LogOperationDTO entity = new LogOperationDTO();
        entity.setUserId(UserContextHolder.userId());
        entity.setClientIp(IpUtil.getIpAddr(request));
        entity.setUserAgent(ServletUtil.getUserAgent());
        entity.setHeaders(JSONUtil.toJsonStr(headers));
        entity.setParameterMap(JSONUtil.toJsonStr(params));
        entity.setPath(request.getRequestURI());
        entity.setMethod(request.getMethod());
        entity.setCreatedTime(UserContextHolder.getSession().getNow());
        entity.setTimeTaken(System.currentTimeMillis() - startTime);
        entity.setTraceId(UserContextHolder.getSession().getTraceId());
        return entity;
    }


    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static final List<String> PATH_LIST = new ArrayList<>();

    static {
        PATH_LIST.add(GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS);
    }


    /**
     * 忽略Filter
     *
     * @param path 路径
     * @return boolean
     */
    public boolean ignoreFilter(String path, List<String> list) {
        return list.stream().anyMatch(url -> path.startsWith(url) || PATH_MATCHER.match(url, path));
    }
}
