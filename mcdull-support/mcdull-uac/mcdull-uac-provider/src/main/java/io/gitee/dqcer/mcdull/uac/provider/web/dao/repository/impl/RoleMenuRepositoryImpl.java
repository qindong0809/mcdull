package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleMenuDO;
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
public class RoleMenuRepositoryImpl extends ServiceImpl<RoleMenuMapper, RoleMenuDO> implements IRoleMenuRepository {


    @Override
    public Map<Integer, List<Integer>> menuIdListMap(Collection<Integer> roleIdCollection) {
        if (ObjectUtil.isNull(roleIdCollection)) {
            throw new IllegalArgumentException("'roleIdCollection' is null");
        }
        LambdaQueryWrapper<RoleMenuDO> query = Wrappers.lambdaQuery();
        query.in(RoleMenuDO::getRoleId, roleIdCollection);
        List<RoleMenuDO> list = baseMapper.selectList(query);
        return list.stream().collect(Collectors.groupingBy(RoleMenuDO::getRoleId,
                Collectors.mapping(RoleMenuDO::getMenuId, Collectors.toList())));
    }

    @Override
    public List<RoleMenuDO> listByRoleId(Integer roleId) {
        LambdaQueryWrapper<RoleMenuDO> query = Wrappers.lambdaQuery();
        query.eq(RoleMenuDO::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

    @Override
    public void insert(Integer roleId, List<Integer> menuIdList) {
        List<RoleMenuDO> list = new ArrayList<>();
        for (Integer menuId : menuIdList) {
            RoleMenuDO roleMenu = new RoleMenuDO();
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
