package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IRoleMenuRepository;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Role Menu Service Impl
 *
 * @author dqcer
 * @since 2024/7/25 9:49
 */

@Service
public class RoleMenuServiceImpl
        extends BasicServiceImpl<IRoleMenuRepository> implements IRoleMenuService {

    @Override
    public Map<Integer, List<Integer>> getMenuIdListMap(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            return baseRepository.menuIdListMap(roleIdList);
        }
        return MapUtil.empty();
    }

    @Override
    public boolean deleteAndInsert(Integer roleId, List<Integer> menuIdList) {
        List<RoleMenuEntity> list = baseRepository.listByRoleId(roleId);
        if (CollUtil.isNotEmpty(list)) {
            baseRepository.removeByIds(list);
        }
        if (CollUtil.isNotEmpty(menuIdList)) {
            baseRepository.insert(roleId, menuIdList);
        }
        return true;
    }
}
