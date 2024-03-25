package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleMenuDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
@Service
public class RoleMenuServiceImpl extends BasicServiceImpl<IRoleMenuRepository> implements IRoleMenuService {

    @Override
    public Map<Integer, List<Integer>> getMenuIdListMap(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            return baseRepository.menuIdListMap(roleIdList);
        }
        return MapUtil.empty();
    }

    @Override
    public boolean deleteAndInsert(Integer roleId, List<Integer> menuIdList) {
        List<RoleMenuDO> list = baseRepository.listByRoleId(roleId);
        if (CollUtil.isNotEmpty(list)) {
            baseRepository.removeBatchByIds(list);
        }
        if (CollUtil.isNotEmpty(menuIdList)) {
            baseRepository.insert(roleId, menuIdList);
        }
        return true;
    }

    @Override
    public Map<Integer, List<Integer>> getRoleIdMap(List<Integer> menuIdList) {
        if (CollUtil.isNotEmpty(menuIdList)) {
            return baseRepository.listByMenuIdList(menuIdList);
        }
        return Collections.emptyMap();
    }

}
