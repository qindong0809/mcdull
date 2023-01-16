package io.gitee.dqcer.uac.provider.web.manager.uac;

import io.gitee.dqcer.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.uac.provider.model.vo.RoleVO;

/**
 * 角色 通用逻辑定义
 *
 * @author dqcer
 * @date 2022/12/26
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
