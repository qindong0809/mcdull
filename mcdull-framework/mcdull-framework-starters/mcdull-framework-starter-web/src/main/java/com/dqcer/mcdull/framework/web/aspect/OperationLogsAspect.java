package io.gitee.dqcer.framework.web.aspect;

import io.gitee.dqcer.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.framework.base.util.JsonUtil;
import io.gitee.dqcer.framework.web.feign.model.LogOperationDTO;
import io.gitee.dqcer.framework.web.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志
 *
 * @author dqcer
 * @version 2022/12/26
 */
@Aspect
@Order(GlobalConstant.Order.ASPECT_OPERATION_LOG)
public class OperationLogsAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogsAspect.class);

    /**
     * 操作日志拦截 ..* 表示任意包或子包
     */
    @Pointcut("execution(* io.github..*.controller..*.*(..))")
    public void operationLogsCut() {
        // 切点
    }

    @Around("operationLogsCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            log.warn("requestAttributes为null");
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(UnAuthorize.class)) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = requestAttributes.getRequest();
        String requestUrl = request.getRequestURI();

        if (log.isDebugEnabled()) {
            log.debug("Operation Logs Url:{}", requestUrl);
        }

        if (ignoreFilter(requestUrl, PATH_LIST)) {
            return joinPoint.proceed();
        }

        if (!isInterceptor(request, method)) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            Object[] args = joinPoint.getArgs();
            LogOperationDTO entity = listenerLog(request, args, startTime);
            RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
            if (log.isDebugEnabled()) {
                log.debug("Operation log dto: {}", entity);
            }
            saveLog(entity, method);
        }

    }

    /**
     * 是拦截器
     *
     * @param request 请求
     * @param method  方法
     * @return boolean
     */
    protected boolean isInterceptor(HttpServletRequest request, Method method) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.GET.name())) {
            return true;
        }
        return false;
    }

    /**
     * 保存日志
     *
     * @param dto dto
     */
    protected void saveLog(LogOperationDTO dto, Method method) {
        //logEventListener.listenLog(entity);
    }

    private LogOperationDTO listenerLog(HttpServletRequest request, Object[] args, long startTime) {

        Map<String, String> headers = new HashMap<>(16);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }

        Map<String, String> params = new HashMap<>(16);

        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                continue;
            }
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            if (arg instanceof InputStreamSource) {
                continue;
            }
            String string = JsonUtil.toJsonString(arg);
            params.put(arg.getClass().getName(), string);
        }

        LogOperationDTO entity = new LogOperationDTO();
        entity.setAccountId(UserContextHolder.getSession().getUserId());
        entity.setClientIp(IpUtil.getIpAddr(request));
        entity.setUserAgent(getUserAgent(request));
        entity.setHeaders(JsonUtil.toJsonString(headers));
        entity.setParameterMap(JsonUtil.toJsonString(params).replaceAll("\\\\", ""));
        entity.setPath(request.getRequestURI());
        entity.setMethod(request.getMethod());
        entity.setCreatedTime(new Date());
        entity.setTimeTaken(System.currentTimeMillis() - startTime);
        entity.setTraceId(UserContextHolder.getSession().getTraceId());
        return entity;
    }

    private static String getUserAgent(HttpServletRequest request) {
        String userAgent = null;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            if ("User-Agent".equalsIgnoreCase(element)) {
                userAgent = request.getHeader(element);
            }
        }
        return userAgent;
    }


    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static final List<String> PATH_LIST = new ArrayList<>();

    static {
        PATH_LIST.add(GlobalConstant.INNER_API + "/**");
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
