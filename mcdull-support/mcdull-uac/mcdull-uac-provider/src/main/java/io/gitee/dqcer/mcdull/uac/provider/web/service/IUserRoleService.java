package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IUserRoleService {
    void deleteAndInsert(Integer userId, List<Integer> roleList);

    Map<Integer, List<Integer>> getRoleIdListMap(List<Integer> userIdList);
}
