package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import org.springframework.stereotype.Service;

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
}
