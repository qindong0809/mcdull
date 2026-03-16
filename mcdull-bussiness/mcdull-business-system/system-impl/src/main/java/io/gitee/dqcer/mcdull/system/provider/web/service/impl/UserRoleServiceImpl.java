package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleUserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.RoleUserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User Role Service Impl
 *
 * @author dqcer
 * @since 2024/01/29
 */
@Service
public class UserRoleServiceImpl
        extends BasicCurdServiceImpl<RoleUserMapper, RoleUserEntity> implements IUserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUserListByRoleId(Integer userId, List<Integer> roleList) {
        this.insert(userId, roleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUserListByRoleId(List<Integer> userIdList, Integer roleId) {
        List<RoleUserEntity> roleUserEntities = this.list(userIdList, roleId);
        if (CollUtil.isNotEmpty(roleUserEntities)) {
            LogicCheckUtil.throwDataExistException(StrUtil.format("roleId: {} userIdList: {}", roleId, userIdList));
        }
        this.insert(userIdList, roleId);
    }

    @Override
    public Map<Integer, List<Integer>> getRoleIdListMap(List<Integer> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            return this.roleIdListMap(userIdList);
        }
        return MapUtil.empty();
    }

    @Override
    public List<Integer> getUserId(Integer roleId) {
        return this.listByRole(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemoveUserListByRole(Integer roleId, List<Integer> userList) {
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleUserEntity::getRoleId, roleId);
        query.in(RoleUserEntity::getUserId, userList);
        this.remove(query);
    }


    public void insert(Integer userId, List<Integer> roleIds) {
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleUserEntity::getUserId, userId);
        baseMapper.delete(query);
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        for (Integer roleId : roleIds) {
            RoleUserEntity entity = new RoleUserEntity();
            entity.setRoleId(roleId);
            entity.setUserId(userId);
            baseMapper.insert(entity);
        }
    }

    public Map<Integer, List<Integer>> roleIdListMap(Collection<Integer> userCollection) {
        List<RoleUserEntity> list = this.list(ListUtil.toList(userCollection));
        return list.stream().collect(Collectors.groupingBy(RoleUserEntity::getUserId,
            Collectors.mapping(RoleUserEntity::getRoleId, Collectors.toList())));
    }

    public List<RoleUserEntity> list(List<Integer> userIdList) {
        if (CollUtil.isEmpty(userIdList)) {
            throw new IllegalArgumentException("'userIdList' is null");
        }
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.in(RoleUserEntity::getUserId, userIdList);
        return baseMapper.selectList(query);
    }

    public List<Integer> listByRole(Integer roleId) {
        if (ObjectUtil.isNotNull(roleId)) {
            LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
            query.in(RoleUserEntity::getRoleId, roleId);
            List<RoleUserEntity> roleUserEntities = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(roleUserEntities)) {
                return roleUserEntities.stream().map(RoleUserEntity::getUserId).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public void insert(List<Integer> userIdList, Integer roleId) {
        List<RoleUserEntity> entities = new ArrayList<>();
        for (Integer userId : userIdList) {
            RoleUserEntity entity = new RoleUserEntity();
            entity.setRoleId(roleId);
            entity.setUserId(userId);
            entities.add(entity);
        }
        super.executeBatch(entities, (sqlSession, entity) -> {
            baseMapper.insert(entity);
        });
    }

    public List<RoleUserEntity> list(List<Integer> userIdList, Integer roleId) {
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.in(RoleUserEntity::getUserId, userIdList);
        query.eq(RoleUserEntity::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

}
