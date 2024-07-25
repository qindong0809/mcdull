package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * User Role Service
 *
 * @author dqcer
 * @since 2024/7/25 9:29
 */
public interface IUserRoleService {

    void batchUserListByRoleId(Integer userId, List<Integer> roleList);

    void batchUserListByRoleId(List<Integer> userIdList, Integer roleId);

    Map<Integer, List<Integer>> getRoleIdListMap(List<Integer> userIdList);

    List<Integer> getUserId(Integer roleId);

    void batchRemoveUserListByRole(Integer roleId, List<Integer> userList);
}
