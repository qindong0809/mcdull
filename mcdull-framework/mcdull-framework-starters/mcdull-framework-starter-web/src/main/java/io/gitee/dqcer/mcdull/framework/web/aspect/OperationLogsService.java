package io.gitee.dqcer.mcdull.framework.web.aspect;

import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 操作日志服务
 *
 * @author dqcer
 * @since 2023/01/29
 */
public interface OperationLogsService {

    /**
     * 是否需要拦截器
     *
     * @param request 请求
     * @param method  方法
     * @return boolean
     */
    boolean needInterceptor(HttpServletRequest request, Method method);

    /**
     * 保存日志
     *
     * @param dto    dto
     * @param method 方法
     */
    void saveLog(LogOperationDTO dto, Method method);
}
