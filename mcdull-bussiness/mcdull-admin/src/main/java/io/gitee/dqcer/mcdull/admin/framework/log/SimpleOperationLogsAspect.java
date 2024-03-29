package io.gitee.dqcer.mcdull.admin.framework.log;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogEntity;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 简单的操作日志拦截
 *
 * @author dqcer
 * @since 2023/01/15 15:01:22
 */
@Component
public class SimpleOperationLogsAspect implements OperationLogsService {

    @Resource
    private OperationLogAsyncEvent asyncEvent;

    /**
     * 是否需要拦截器
     *
     * @param request 请求
     * @param method  方法
     * @return boolean
     */
    @Override
    public boolean needInterceptor(HttpServletRequest request, Method method) {
//        return method.isAnnotationPresent(OperationLog.class);
        return method.isAnnotationPresent(Authorized.class);
    }



    @Override
    public void saveLog(LogOperationDTO dto, Method method) {
//        OperationLog annotation = method.getAnnotation(OperationLog.class);
//        LogDO logDO = of(dto);
//        logDO.setModel(annotation.module());
//        logDO.setMenu(annotation.menu());
//        logDO.setType(annotation.type().getCode());
//        asyncEvent.asyncEvent(logDO);
        Authorized annotation = method.getAnnotation(Authorized.class);
        LogEntity logDO = of(dto);
//        logDO.setModel(annotation.value());
//        logDO.setMenu(annotation.menu());
        logDO.setButton(annotation.value());
        asyncEvent.asyncEvent(logDO);

    }

    private static LogEntity of(LogOperationDTO dto) {
        LogEntity logDO = new LogEntity();
        logDO.setAccountId(dto.getUserId());
        logDO.setTenantId(dto.getTenantId());
        logDO.setClientIp(dto.getClientIp());
        logDO.setUserAgent(dto.getUserAgent());
        logDO.setMethod(dto.getMethod());
        logDO.setPath(dto.getPath());
        logDO.setTraceId(dto.getTraceId());
        logDO.setTimeTaken(dto.getTimeTaken());
        logDO.setParameterMap(dto.getParameterMap());
        logDO.setHeaders(dto.getHeaders());
        logDO.setCreatedTime(dto.getCreatedTime());
        return logDO;

    }
}
