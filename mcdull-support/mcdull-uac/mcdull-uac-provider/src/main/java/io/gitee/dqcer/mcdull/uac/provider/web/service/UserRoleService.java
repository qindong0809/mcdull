package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dqcer
 * @since 2024/01/29
 */
@Service
public class UserRoleService {

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Transactional(rollbackFor = Exception.class)
    public void deleteAndInsert(Long userId, List<Long> roleList) {
        userRoleRepository.deleteAndInsert(userId, roleList);
    }
}
