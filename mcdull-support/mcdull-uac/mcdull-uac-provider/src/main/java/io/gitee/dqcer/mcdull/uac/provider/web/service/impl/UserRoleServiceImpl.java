package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleUserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 * @since 2024/01/29
 */
@Service
public class UserRoleServiceImpl extends BasicServiceImpl<IUserRoleRepository>  implements IUserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUserListByRoleId(Long userId, List<Long> roleList) {
        baseRepository.insert(userId, roleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUserListByRoleId(List<Long> userIdList, Long roleId) {
        List<RoleUserEntity> roleUserEntities = baseRepository.list(userIdList, roleId);
        if (CollUtil.isNotEmpty(roleUserEntities)) {
            this.throwDataExistException(StrUtil.format("roleId: {} userIdList: {}", roleId, userIdList));
        }
        baseRepository.insert(userIdList, roleId);
    }

    @Override
    public Map<Long, List<Long>> getRoleIdListMap(List<Long> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            return baseRepository.roleIdListMap(userIdList);
        }
        return MapUtil.empty();
    }

    @Override
    public List<Long> getUserId(Long roleId) {
        return baseRepository.listByRole(roleId);
    }
}
