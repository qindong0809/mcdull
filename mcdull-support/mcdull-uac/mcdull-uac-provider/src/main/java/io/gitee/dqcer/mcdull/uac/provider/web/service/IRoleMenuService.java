package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IRoleMenuService {
    Map<Integer, List<Integer>> getMenuIdListMap(List<Integer> roleIdList);

    boolean deleteAndInsert(Integer id, List<Integer> menuIdList);

    Map<Integer, List<Integer>> getRoleIdMap(List<Integer> menuIdList);
}
