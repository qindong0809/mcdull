package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
@Service
public class MenuServiceImpl extends BasicServiceImpl<IMenuRepository>  implements IMenuService {

    @Resource
    private IRoleMenuService roleMenuService;

    @Override
    public Map<Long, List<String>> getMenuCodeListMap(List<Long> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Long, List<Long>> menuListMap = roleMenuService.getMenuIdListMap(roleIdList);
            return baseRepository.menuCodeListMap(menuListMap);
        }
        return MapUtil.empty();
    }

    @Override
    public List<String> getAllCodeList() {
        return baseRepository.allCodeList();
    }
}
