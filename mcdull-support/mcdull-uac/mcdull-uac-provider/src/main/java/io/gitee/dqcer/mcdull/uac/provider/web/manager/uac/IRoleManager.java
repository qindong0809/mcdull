package io.gitee.dqcer.mcdull.uac.provider.web.manager.uac;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;

/**
 * 角色 通用逻辑定义
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleManager {

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link RoleVO}
     */
    RoleVO entity2VO(RoleDO entity);
}
