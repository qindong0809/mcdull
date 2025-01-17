package io.gitee.dqcer.mcdull.admin.web.manager.common.impl;

import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigEntity;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 用户通用逻辑实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class SysConfigManagerImpl implements ISysConfigManager {

    @Resource
    private ISysConfigRepository sysConfigRepository;

    /**
     * 通过枚举找到value
     *
     * @param keyEnum 实体
     * @return {@link String}
     */
    @Override
    public String findValueByEnum(SysConfigKeyEnum keyEnum) {
        SysConfigEntity sysConfigDO = sysConfigRepository.findByKey(keyEnum.getCode());
        if (sysConfigDO == null) {
            throw new DatabaseRowException(CodeEnum.DATA_NOT_EXIST);
        }
        return sysConfigDO.getConfigValue();
    }
}
