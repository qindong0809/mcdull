package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditUtil;
import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IBizAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
}
