package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IUserRoleService {
    void batchUserListByRoleId(Long userId, List<Long> roleList);

    void batchUserListByRoleId(List<Long> userIdList, Long roleId);

    Map<Long, List<Long>> getRoleIdListMap(List<Long> userIdList);

    List<Long> getUserId(Long roleId);
}
