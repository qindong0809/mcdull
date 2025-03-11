package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * Role Menu Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IRoleMenuService {
    Map<Integer, List<Integer>> getMenuIdListMap(List<Integer> roleIdList);

    boolean deleteAndInsert(Integer id, List<Integer> menuIdList);
}
