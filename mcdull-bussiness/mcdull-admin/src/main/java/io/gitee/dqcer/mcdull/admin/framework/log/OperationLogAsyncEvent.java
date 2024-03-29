package io.gitee.dqcer.mcdull.admin.framework.log;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogEntity;
import io.gitee.dqcer.mcdull.framework.web.async.BaseAsyncEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 简单的基本事件监听器
 *
 * @author dqcere
 * @since 2023/01/15 16:01:34
 */
@Component
public class OperationLogAsyncEvent extends BaseAsyncEvent<LogEntity> {

    @Resource
    private IOperationLog operationLog;

    /**
     * 执行
     *
     * @param t t
     */
    @Override
    protected void execute(LogEntity t) {
        operationLog.save(t);
    }


}
