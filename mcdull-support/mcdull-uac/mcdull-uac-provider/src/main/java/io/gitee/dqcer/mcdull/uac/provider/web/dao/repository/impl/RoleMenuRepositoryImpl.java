package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleMenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.RoleMenuMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleMenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
public class RoleMenuRepositoryImpl extends ServiceImpl<RoleMenuMapper, RoleMenuEntity> implements IRoleMenuRepository {


    @Override
    public Map<Long, List<Long>> menuIdListMap(Collection<Long> roleIdCollection) {
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
    public List<RoleMenuEntity> listByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenuEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleMenuEntity::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

    @Override
    public void insert(Long roleId, List<Long> menuIdList) {
        List<RoleMenuEntity> list = new ArrayList<>();
        for (Long menuId : menuIdList) {
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

    @Override
    public Map<Long, List<Long>> listByMenuIdList(List<Long> menuIdList) {
        if (ObjectUtil.isNull(menuIdList)) {
            throw new IllegalArgumentException("'menuIdList' is null");
        }
        LambdaQueryWrapper<RoleMenuEntity> query = Wrappers.lambdaQuery();
        query.in(RoleMenuEntity::getMenuId, menuIdList);
        List<RoleMenuEntity> list = baseMapper.selectList(query);
        return list.stream().collect(Collectors.groupingBy(RoleMenuEntity::getMenuId,
                Collectors.mapping(RoleMenuEntity::getRoleId, Collectors.toList())));
    }
}
