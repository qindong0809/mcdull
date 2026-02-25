package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleUserEntity;

import java.util.List;
import java.util.Map;

/**
 * User Role Service
 *
 * @author dqcer
 * @since 2024/7/25 9:29
 */
public interface IUserRoleService extends IRepository<RoleUserEntity> {

    /**
     * batch save
     *
     * @param userId   userId
     * @param roleList roleList
     */
    void batchUserListByRoleId(Integer userId, List<Integer> roleList);

    /**
     * batch save
     *
     * @param userIdList userIdList
     * @param roleId     roleId
     */
    void batchUserListByRoleId(List<Integer> userIdList, Integer roleId);

    /**
     * map
     *
     * @param userIdList userIdList
     * @return {@link Map }<{@link Integer }, {@link List }<{@link Integer }>>
     */
    Map<Integer, List<Integer>> getRoleIdListMap(List<Integer> userIdList);

    /**
     * 获取用户ID
     *
     * @param roleId 角色ID
     * @return {@link List }<{@link Integer }>
     */
    List<Integer> getUserId(Integer roleId);

    /**
     * batch remove
     *
     * @param roleId   roleId
     * @param userList userList
     */
    void batchRemoveUserListByRole(Integer roleId, List<Integer> userList);
}
