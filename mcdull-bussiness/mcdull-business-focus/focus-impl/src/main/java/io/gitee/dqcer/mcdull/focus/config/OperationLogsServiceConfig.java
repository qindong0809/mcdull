package io.gitee.dqcer.mcdull.focus.config;

import io.gitee.dqcer.mcdull.framework.web.aspect.OperationLogsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class OperationLogsServiceConfig implements OperationLogsService {

    @Override
    public boolean needInterceptor(HttpServletRequest request, Method method) {
        return false;
    }

    @Override
    public void saveLog(LogOperationDTO dto, Method method) {

    }
}
