package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserRoleDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.UserRoleMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * 更新根据用户id
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    @Override
    public void deleteAndInsert(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<UserRoleDO> query = Wrappers.lambdaQuery();
        query.eq(UserRoleDO::getUserId, userId);
        baseMapper.delete(query);
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

    @Override
    public Map<Long, List<Long>> roleIdListMap(Collection<Long> userCollection) {
        if (ObjectUtil.isNull(userCollection)) {
            throw new IllegalArgumentException("'userCollection' is null");
        }
        LambdaQueryWrapper<UserRoleDO> query = Wrappers.lambdaQuery();
        query.in(UserRoleDO::getUserId, userCollection);
        List<UserRoleDO> list = baseMapper.selectList(query);
        return list.stream().collect(Collectors.groupingBy(UserRoleDO::getUserId,
                Collectors.mapping(UserRoleDO::getRoleId, Collectors.toList())));
    }
}
