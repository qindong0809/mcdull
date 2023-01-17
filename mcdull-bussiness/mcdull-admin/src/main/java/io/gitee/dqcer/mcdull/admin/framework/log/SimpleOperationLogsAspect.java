package io.gitee.dqcer.mcdull.admin.framework.log;

import io.gitee.dqcer.mcdull.admin.config.OperationLog;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsAspect;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 简单的操作日志拦截
 *
 * @author dqcer
 * @version 2023/01/15 15:01:22
 */
@Component
public class SimpleOperationLogsAspect extends OperationLogsAspect {

    @Resource
    private OperationLogAsyncEvent asyncEvent;

    /**
     * 是拦截器
     *
     * @param request 请求
     * @param method  方法
     * @return boolean
     */
    @Override
    protected boolean isInterceptor(HttpServletRequest request, Method method) {
        return method.isAnnotationPresent(OperationLog.class);
    }

    @Override
    protected void saveLog(LogOperationDTO dto, Method method) {
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        LogDO logDO = of(dto);
        logDO.setModel(annotation.module());
        logDO.setMenu(annotation.menu());
        logDO.setType(annotation.type().getCode());
        asyncEvent.asyncEvent(logDO);
    }

    private static LogDO of(LogOperationDTO dto) {
        LogDO logDO = new LogDO();
        logDO.setAccountId(dto.getAccountId());
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
