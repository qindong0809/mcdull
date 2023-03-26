package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuDO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IRoleManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户通用逻辑实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class RoleManagerImpl implements IRoleManager {


    @Resource
    private IMenuRepository menuRepository;

    @Resource
    private IRoleRepository roleRepository;



    /**
     * 获取菜单
     *
     * @param roleId 角色id
     * @return {@link List}<{@link MenuDO}>
     */
    @Override
    public List<MenuDO> getMenuByRole(List<Long> roles) {
        List<RoleMenuDO> list = roleRepository.getMenuByRole(roles);
        List<Long> menuIds = list.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toList());
        if (menuIds.isEmpty()) {
            return Collections.emptyList();
        }
        return menuRepository.getMenuByIds(menuIds);
    }
}
