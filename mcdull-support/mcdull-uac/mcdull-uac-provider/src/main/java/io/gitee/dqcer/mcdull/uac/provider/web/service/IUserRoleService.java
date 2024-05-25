package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IUserRoleService {
    void batchUserListByRoleId(Integer userId, List<Integer> roleList);

    void batchUserListByRoleId(List<Integer> userIdList, Integer roleId);

    Map<Integer, List<Integer>> getRoleIdListMap(List<Integer> userIdList);

    List<Integer> getUserId(Integer roleId);
}
