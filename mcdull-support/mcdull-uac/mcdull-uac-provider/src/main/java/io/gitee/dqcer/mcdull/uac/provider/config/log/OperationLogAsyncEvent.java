package io.gitee.dqcer.mcdull.uac.provider.config.log;

import io.gitee.dqcer.mcdull.framework.web.async.BaseAsyncEvent;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dqcere
 * @since 2023/01/15 16:01:34
 */
@Component
public class OperationLogAsyncEvent extends BaseAsyncEvent<OperateLogEntity> {

    @Resource
    private IOperationLog operationLog;

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
