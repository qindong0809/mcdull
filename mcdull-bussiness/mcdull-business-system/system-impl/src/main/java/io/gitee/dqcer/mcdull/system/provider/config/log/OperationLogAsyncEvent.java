package io.gitee.dqcer.mcdull.system.provider.config.log;

import io.gitee.dqcer.mcdull.framework.web.async.BaseAsyncEvent;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.system.provider.web.service.IOperateLogService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @author dqcere
 * @since 2023/01/15 16:01:34
 */
@Component
public class OperationLogAsyncEvent extends BaseAsyncEvent<OperateLogEntity> {

    @Resource
    private IOperateLogService operationLog;

    /**
     * 执行
     *
     * @param t t
     */
    @Override
    protected void execute(OperateLogEntity t) {
        operationLog.save(t);
    }


}
