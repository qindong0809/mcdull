package com.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.mcdull.framework.base.constants.GlobalConstant;
import com.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import com.dqcer.mcdull.framework.base.util.ObjUtil;
import com.dqcer.mcdull.admin.model.entity.sys.UserRoleDO;
import com.dqcer.mcdull.admin.web.dao.mapper.sys.UserRoleMapper;
import com.dqcer.mcdull.admin.web.dao.repository.sys.IUserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色 数据库操作封装实现层
 *
 * @author dqcer
 * @version 2022/12/25
 */
@Service
public class UserRoleRepositoryImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements IUserRoleRepository {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> listRoleByUserId(Long userId) {
        LambdaQueryWrapper<UserRoleDO> query = Wrappers.lambdaQuery();
        query.eq(UserRoleDO::getUserId, userId);
        List<UserRoleDO> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
    }

    /**
     * 更新根据用户id
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    @Override
    public void updateByUserId(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<UserRoleDO> query = Wrappers.lambdaQuery();
        query.eq(UserRoleDO::getUserId, userId);
        int row = baseMapper.delete(query);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException();
        }


        if (ObjUtil.isNull(roleIds)) {
            return;
        }

        List<UserRoleDO> entities = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRoleDO entity = new UserRoleDO();
            entity.setRoleId(roleId);
            entity.setUserId(userId);
            entities.add(entity);
        }
        boolean saveBatch = saveBatch(entities);
        if (!saveBatch) {
            throw new DatabaseRowException();
        }
    }
}
