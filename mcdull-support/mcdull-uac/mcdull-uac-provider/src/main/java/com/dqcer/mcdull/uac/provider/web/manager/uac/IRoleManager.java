package com.dqcer.mcdull.uac.provider.web.manager.uac;

import com.dqcer.mcdull.uac.api.entity.RoleDO;
import com.dqcer.mcdull.uac.api.vo.RoleVO;

public interface IRoleManager {

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link RoleVO}
     */
    RoleVO entity2VO(RoleDO entity);
}
