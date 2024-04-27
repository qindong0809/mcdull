package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IUserRoleService {
    void deleteAndInsert(Long userId, List<Long> roleList);

    Map<Long, List<Long>> getRoleIdListMap(List<Long> userIdList);
}
