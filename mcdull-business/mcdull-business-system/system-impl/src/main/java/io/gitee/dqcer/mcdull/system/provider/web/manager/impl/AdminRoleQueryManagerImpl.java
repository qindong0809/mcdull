package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminRoleEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAdminRoleQueryManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.administrator.AdminRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 管理端角色只读查询实现。
 */
@Service
public class AdminRoleQueryManagerImpl implements IAdminRoleQueryManager {

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    public List<AdminRoleEntity> listByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return adminRoleMapper.selectBatchIds(ids);
    }
}
