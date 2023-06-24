package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserRoleDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.UserRoleMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRoleRepositoryImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements IUserRoleRepository {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserRoleDO}>
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
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
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
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}
