package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
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
    public void deleteAndInsert(Long userId, List<Long> roleList) {
        baseRepository.deleteAndInsert(userId, roleList);
    }

    @Override
    public Map<Long, List<Long>> getRoleIdListMap(List<Long> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            return baseRepository.roleIdListMap(userIdList);
        }
        return MapUtil.empty();
    }
}
