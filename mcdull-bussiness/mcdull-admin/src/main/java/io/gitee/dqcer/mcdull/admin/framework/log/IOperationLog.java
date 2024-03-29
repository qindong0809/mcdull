package io.gitee.dqcer.mcdull.admin.framework.log;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogEntity;

/**
 * 操作日志
 *
 * @author dqcer
 * @since 2023/01/15 15:01:03
 */
public interface IOperationLog {

    /**
     * 保存
     *
     * @param dto dto
     */
    void save(LogEntity dto);
}
