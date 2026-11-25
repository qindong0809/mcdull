package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAdminUserQueryManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.administrator.AdminUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理端用户只读查询实现，语义与原 IAdminUserService.getUserNameMap 一致。
 */
@Service
public class AdminUserQueryManagerImpl implements IAdminUserQueryManager {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public Map<Integer, String> getUserNameMap() {
        List<AdminUserEntity> list = adminUserMapper.selectList(null);
        return list.stream().collect(Collectors.toMap(AdminUserEntity::getId, AdminUserEntity::getActualName));
    }
}
