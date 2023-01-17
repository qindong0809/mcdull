package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserRoleDO;

import java.util.List;

/**
 * 用户角色 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/26
 */
public interface IUserRoleRepository extends IService<UserRoleDO> {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserRoleDO}>
     */
    List<Long> listRoleByUserId(Long userId);

    /**
     * 更新根据用户id
     *
     * @param id      id
     * @param roleIds 角色id
     */
    void updateByUserId(Long id, List<Long> roleIds);
}
