package io.gitee.dqcer.mcdull.admin.web.manager.sys;


import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;

import java.util.List;

/**
 * 菜单 通用逻辑定义
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleManager {

    /**
     * 获取菜单
     *
     * @param roleId 角色id
     * @return {@link List}<{@link MenuDO}>
     */
    List<MenuDO> getMenuByRole(List<Long> roles);
}
