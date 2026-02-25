package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.RoleMenuMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Role Menu Service Impl
 *
 * @author dqcer
 * @since 2024/7/25 9:49
 */

@Service
public class RoleMenuServiceImpl
        extends BasicCurdServiceImpl<RoleMenuMapper, RoleMenuEntity> implements IRoleMenuService {

    @Override
    public Map<Integer, List<Integer>> getMenuIdListMap(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            return this.menuIdListMap(roleIdList);
        }
        return MapUtil.empty();
    }

    @Override
    public boolean deleteAndInsert(Integer roleId, List<Integer> menuIdList) {
        List<RoleMenuEntity> list = this.listByRoleId(roleId);
        if (CollUtil.isNotEmpty(list)) {
            super.removeByIds(list);
        }
        if (CollUtil.isNotEmpty(menuIdList)) {
            this.insert(roleId, menuIdList);
        }
        return true;
    }

    public Map<Integer, List<Integer>> menuIdListMap(Collection<Integer> roleIdCollection) {
        if (ObjectUtil.isNull(roleIdCollection)) {
            throw new IllegalArgumentException("'roleIdCollection' is null");
        }
        LambdaQueryWrapper<RoleMenuEntity> query = Wrappers.lambdaQuery();
        query.in(RoleMenuEntity::getRoleId, roleIdCollection);
        List<RoleMenuEntity> list = baseMapper.selectList(query);
        return list.stream().collect(Collectors.groupingBy(RoleMenuEntity::getRoleId,
            Collectors.mapping(RoleMenuEntity::getMenuId, Collectors.toList())));
    }

    public List<RoleMenuEntity> listByRoleId(Integer roleId) {
        LambdaQueryWrapper<RoleMenuEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleMenuEntity::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

    public void insert(Integer roleId, List<Integer> menuIdList) {
        List<RoleMenuEntity> list = new ArrayList<>();
        for (Integer menuId : menuIdList) {
            RoleMenuEntity roleMenu = new RoleMenuEntity();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            list.add(roleMenu);
        }
        this.executeBatch(list, list.size(), (sqlSession, roleMenuDO) -> {
            RoleMenuMapper roleMenuMapper = sqlSession.getMapper(RoleMenuMapper.class);
            roleMenuMapper.insert(roleMenuDO);
        });
    }
}
