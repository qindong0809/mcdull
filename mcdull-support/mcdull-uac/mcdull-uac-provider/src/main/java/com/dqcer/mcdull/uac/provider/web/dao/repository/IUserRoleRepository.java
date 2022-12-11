package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.uac.api.entity.UserRoleDO;

import java.util.List;

public interface IUserRoleRepository extends IService<UserRoleDO> {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserRoleDO}>
     */
    List<Long> listRoleByUserId(Long userId);

    void updateByUserId(Long id, List<Long> roleIds);
}
