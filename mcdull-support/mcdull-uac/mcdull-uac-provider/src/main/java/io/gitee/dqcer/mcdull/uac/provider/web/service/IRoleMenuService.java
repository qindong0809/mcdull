package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IRoleMenuService {
    Map<Long, List<Long>> getMenuIdListMap(List<Long> roleIdList);

    boolean deleteAndInsert(Long id, List<Long> menuIdList);

    Map<Long, List<Long>> getRoleIdMap(List<Long> menuIdList);
}
