package io.gitee.dqcer.mcdull.uac.provider.config.log;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
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
        Authorized annotation = method.getAnnotation(Authorized.class);
        LoginLogEntity logDO = of(dto);
        asyncEvent.asyncEvent(logDO);

    }

    private static LoginLogEntity of(LogOperationDTO dto) {
        LoginLogEntity logDO = new LoginLogEntity();
//        logDO.setLoginName(Convert.toLong(dto.getUserId()));
        logDO.setUserAgent(dto.getUserAgent());
        logDO.setLoginIp(dto.getClientIp());
        logDO.setLoginIpRegion(dto.getClientIp());
//        logDO.setPath(dto.getPath());
//        logDO.setHeaders(dto.getHeaders());
//        logDO.setParameterMap(dto.getParameterMap());
//        logDO.setTraceId(dto.getTraceId());
//        logDO.setCreatedTime(dto.getCreatedTime());
//        logDO.setTimeTaken(dto.getTimeTaken());
//        logDO.setMethod(dto.getMethod());
        return logDO;

    }
}
