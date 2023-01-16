package io.gitee.dqcer.admin.framework.log;

import io.gitee.dqcer.admin.model.entity.sys.LogDO;

/**
 * 操作日志
 *
 * @author dqcer
 * @date 2023/01/15 15:01:03
 */
public interface IOperationLog {

    /**
     * 保存
     *
     * @param dto dto
     */
    void save(LogDO dto);
}
