package io.gitee.dqcer.admin.framework.log;

import io.gitee.dqcer.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.framework.web.async.BaseAsyncEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 简单的基本事件监听器
 *
 * @author dqcere
 * @date 2023/01/15 16:01:34
 */
@Component
public class OperationLogAsyncEvent extends BaseAsyncEvent<LogDO> {

    @Resource
    private IOperationLog operationLog;

    /**
     * 执行
     *
     * @param t t
     */
    @Override
    protected void execute(LogDO t) {
        operationLog.save(t);
    }


}
