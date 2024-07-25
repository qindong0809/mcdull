package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleUserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.RoleUserMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户角色 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRoleRepositoryImpl
        extends ServiceImpl<RoleUserMapper, RoleUserEntity> implements IUserRoleRepository {

    @Override
    public void insert(Integer userId, List<Integer> roleIds) {
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleUserEntity::getUserId, userId);
        baseMapper.delete(query);
        if (ObjUtil.isNull(roleIds)) {
            return;
        }
        for (Integer roleId : roleIds) {
            RoleUserEntity entity = new RoleUserEntity();
            entity.setRoleId(roleId);
            entity.setUserId(userId);
            baseMapper.insert(entity);
        }
    }

    @Override
    public Map<Integer, List<Integer>> roleIdListMap(Collection<Integer> userCollection) {
        List<RoleUserEntity> list = this.list(ListUtil.toList(userCollection));
        return list.stream().collect(Collectors.groupingBy(RoleUserEntity::getUserId,
                Collectors.mapping(RoleUserEntity::getRoleId, Collectors.toList())));
    }

    @Override
    public List<RoleUserEntity> list(List<Integer> userIdList) {
        if (CollUtil.isEmpty(userIdList)) {
            throw new IllegalArgumentException("'userIdList' is null");
        }
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.in(RoleUserEntity::getUserId, userIdList);
        return baseMapper.selectList(query);
    }

    @Override
    public List<Integer> listByRole(Integer roleId) {
        if (ObjUtil.isNotNull(roleId)) {
            LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
            query.in(RoleUserEntity::getRoleId, roleId);
            List<RoleUserEntity> roleUserEntities = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(roleUserEntities)) {
                return roleUserEntities.stream().map(RoleUserEntity::getUserId).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
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

    @Override
    public List<RoleUserEntity> list(List<Integer> userIdList, Integer roleId) {
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.in(RoleUserEntity::getUserId, userIdList);
        query.eq(RoleUserEntity::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

    @Override
    public void batchRemoveUserListByRole(Integer roleId, List<Integer> userList) {
        LambdaQueryWrapper<RoleUserEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleUserEntity::getRoleId, roleId);
        query.in(RoleUserEntity::getUserId, userList);
        this.remove(query);
    }
}
