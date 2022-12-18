package com.dqcer.mcdull.uac.provider.web.manager.uac;

import com.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import com.dqcer.mcdull.uac.provider.model.vo.RoleVO;

public interface IRoleManager {

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link RoleVO}
     */
    RoleVO entity2VO(RoleDO entity);
}
