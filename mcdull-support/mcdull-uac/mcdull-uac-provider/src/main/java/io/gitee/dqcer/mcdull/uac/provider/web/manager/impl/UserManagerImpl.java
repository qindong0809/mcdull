package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserManagerImpl implements IUserManager {

    @Resource
    private IUserRepository userRepository;

    @Override
    public Map<Integer, String> getNameMap(List<Integer> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            List<UserEntity> entityList = userRepository.listByIds(userIdList);
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
            List<UserEntity> list = userRepository.list();
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().filter(i -> loginList.contains(i.getLoginName()))
                        .collect(Collectors.toMap(UserEntity::getLoginName, UserEntity::getActualName));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public List<UserEntity> getLike(String userName) {
        return userRepository.like(userName);
    }

    @Override
    public Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList) {
        List<UserEntity> list = userRepository.listByIds(userIdList);
        return list.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
    }

    @Override
    public List<LabelValueVO<Integer, String>> getResponsibleList() {
        List<UserEntity> list = userRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            return list.stream()
                    .map(i -> new LabelValueVO<>(i.getId(), i.getActualName()))
                    .sorted(Comparator.comparing(LabelValueVO::getLabel))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
