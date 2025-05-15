package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.RoleMenuMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IRoleMenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Role Menu Repository Impl
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
public class RoleMenuRepositoryImpl
        extends CrudRepository<RoleMenuMapper, RoleMenuEntity> implements IRoleMenuRepository {

    @Override
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

    @Override
    public List<RoleMenuEntity> listByRoleId(Integer roleId) {
        LambdaQueryWrapper<RoleMenuEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleMenuEntity::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

    @Override
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
