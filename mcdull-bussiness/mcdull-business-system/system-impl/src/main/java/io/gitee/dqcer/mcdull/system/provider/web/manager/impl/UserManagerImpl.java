package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDepartmentService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserManagerImpl implements IUserManager {

    @Resource
    private IUserService userService;
    @Resource
    private IDepartmentService departmentService;

    @Override
    public Map<Integer, String> getNameMap(List<Integer> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            List<UserEntity> entityList = userService.listByIds(userIdList);
            if (CollUtil.isNotEmpty(entityList)) {
                return entityList.stream()
                        .collect(Collectors.toMap(UserEntity::getId, UserEntity::getActualName));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> getNameMapByLoginName(List<String> loginList) {
        if (CollUtil.isNotEmpty(loginList)) {
            List<UserEntity> list = userService.list();
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().filter(i -> loginList.contains(i.getLoginName()))
                        .collect(Collectors.toMap(UserEntity::getLoginName, UserEntity::getActualName));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList) {
        List<UserEntity> list = userService.listByIds(userIdList);
        return list.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
    }

    @Override
    public List<LabelValueVO<Integer, String>> getResponsibleList() {
        List<UserEntity> list = userService.list();
        if (CollUtil.isNotEmpty(list)) {
            return list.stream()
                    .map(i -> new LabelValueVO<>(i.getId(), i.getActualName()))
                    .sorted(Comparator.comparing(LabelValueVO::getLabel))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Integer> getUserIdList(Integer departmentId) {
        List<Integer> childrenIdList = CollUtil.defaultIfEmpty(departmentService.getChildrenIdList(departmentId), new ArrayList<>());
        childrenIdList.add(departmentId);
        List<UserEntity> list = userService.listByDeptList(childrenIdList);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(UserEntity::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Integer, String> getMap(List<? extends BaseEntity<Integer>> list) {
        if (CollUtil.isNotEmpty(list)) {
            Set<Integer> createSet = list.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
            Set<Integer> updateSet = list.stream().map(BaseEntity::getUpdatedBy).filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            createSet.addAll(updateSet);
            return this.getNameMap(new ArrayList<>(createSet));
        }
        return Map.of();
    }
}
