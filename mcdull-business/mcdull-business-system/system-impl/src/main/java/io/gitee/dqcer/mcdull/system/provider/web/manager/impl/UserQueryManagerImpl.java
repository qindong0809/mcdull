package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserQueryManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户只读查询实现，查询语义与 UserServiceImpl 中的原方法保持一致。
 */
@Service
public class UserQueryManagerImpl implements IUserQueryManager {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserEntity> getLike(String userName) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.like(UserEntity::getActualName, userName);
        return userMapper.selectList(query);
    }

    @Override
    public Map<Integer, String> getMap(List<? extends BaseEntity<Integer>> list) {
        if (CollUtil.isNotEmpty(list)) {
            Set<Integer> createSet = list.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
            Set<Integer> updateSet = list.stream().map(BaseEntity::getUpdatedBy)
                    .filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            createSet.addAll(updateSet);
            return this.getNameMap(new ArrayList<>(createSet));
        }
        return Map.of();
    }

    @Override
    public Map<Integer, String> getNameMap(List<Integer> userIdList) {
        return this.list(userIdList).stream()
                .collect(Collectors.toMap(IdEntity::getId, UserEntity::getActualName));
    }

    @Override
    public Map<String, String> getNameMapByLoginName(List<String> loginList) {
        if (CollUtil.isNotEmpty(loginList)) {
            List<UserEntity> list = userMapper.selectList(null);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().filter(i -> loginList.contains(i.getLoginName()))
                        .collect(Collectors.toMap(UserEntity::getLoginName, UserEntity::getActualName));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList) {
        return this.list(userIdList).stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
    }

    @Override
    public UserEntity get(Integer userId) {
        if (userId == null) {
            return null;
        }
        return userMapper.selectById(userId);
    }

    @Override
    public List<UserEntity> listByDeptList(List<Integer> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.in(UserEntity::getDepartmentId, deptIdList);
        return userMapper.selectList(query);
    }

    private List<UserEntity> list(List<Integer> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            return CollUtil.emptyIfNull(userMapper.selectBatchIds(userIdList));
        }
        return Collections.emptyList();
    }
}
