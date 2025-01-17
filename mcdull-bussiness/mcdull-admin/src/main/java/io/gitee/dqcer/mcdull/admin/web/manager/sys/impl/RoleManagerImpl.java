package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuEntity;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IRoleManager;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
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


    @Override
    public List<MenuEntity> getMenuByRole(List<Long> roles) {
        List<RoleMenuEntity> list = roleRepository.getMenuByRole(roles);
        List<Long> menuIds = list.stream().map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
        if (menuIds.isEmpty()) {
            return Collections.emptyList();
        }
        return menuRepository.getMenuByIds(menuIds);
    }

}
