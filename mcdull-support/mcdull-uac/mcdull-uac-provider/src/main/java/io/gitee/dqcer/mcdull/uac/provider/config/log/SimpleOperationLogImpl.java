package io.gitee.dqcer.mcdull.uac.provider.config.log;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.util.Ip2RegionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AnnotationUtils;
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
public class SimpleOperationLogImpl implements OperationLogsService {

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
        Tag tag = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Tag.class);
        Operation operation = method.getAnnotation(Operation.class);
        return ObjUtil.isAllNotEmpty(tag, operation);
    }



    @Override
    public void saveLog(LogOperationDTO dto, Method method) {
        Tag tag = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Tag.class);
        Operation operation = method.getAnnotation(Operation.class);
        if (ObjUtil.isNotNull(tag)) {
            if (ObjUtil.isNotNull(operation)) {
                OperateLogEntity entity = this.of(dto, tag.name(), operation.summary());
                if (ObjUtil.isNotNull(entity)) {
                    asyncEvent.asyncEvent(entity);
                }
            }
        }
    }


    private OperateLogEntity of(LogOperationDTO dto, String name, String summary) {
        Integer userId = dto.getUserId();
        if (ObjUtil.isNull(userId)) {
            return null;
        }
        OperateLogEntity entity = new OperateLogEntity();
        entity.setUserId(userId);
        entity.setModule(name);
        entity.setContent(summary);
        entity.setUrl(dto.getPath());
        entity.setMethod(dto.getMethod());
        entity.setParam(dto.getParameterMap());
        String clientIp = dto.getClientIp();
        entity.setIp(clientIp);
        entity.setIpRegion(Ip2RegionUtil.getRegion(clientIp));
        entity.setUserAgent(dto.getUserAgent());
        entity.setFailReason(StrUtil.EMPTY);
        entity.setTraceId(dto.getTraceId());
        entity.setTimeTaken(Convert.toInt(dto.getTimeTaken()));
        entity.setSuccessFlag(dto.getSuccessFlag());
        return entity;
    }
}
