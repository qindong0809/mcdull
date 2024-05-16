package io.gitee.dqcer.mcdull.uac.provider.config.log;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;

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
    void save(LoginLogEntity dto);
}
