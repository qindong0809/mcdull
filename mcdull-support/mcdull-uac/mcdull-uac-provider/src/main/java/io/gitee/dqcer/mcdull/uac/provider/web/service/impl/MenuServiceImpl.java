package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MetaVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RouterVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dqcer
 */
@Service
public class MenuServiceImpl extends BasicServiceImpl<IMenuRepository>  implements IMenuService {

    @Resource
    private IRoleMenuService roleMenuService;

    @Resource
    private IUserRoleService userRoleService;

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

    @Override
    public RouterVO tree(Long userId) {

        Map<Long, List<Long>> roleIdListMap = userRoleService.getRoleIdListMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(roleIdListMap)) {
            List<Long> roleIdList = roleIdListMap.get(userId);
            if (CollUtil.isNotEmpty(roleIdList)) {
                Map<Long, List<Long>> menuIdListMap = roleMenuService.getMenuIdListMap(roleIdList);
                if (MapUtil.isNotEmpty(menuIdListMap)) {
                    Set<Long> idSet = menuIdListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
                    if (CollUtil.isNotEmpty(idSet)) {
                        List<MenuDO> menuList = baseRepository.list(idSet);

                        Tree<Long> integerTree = TreeUtil.buildSingle(menuList, 0L, new NodeParser<MenuDO, Long>() {
                            @Override
                            public void parse(MenuDO menu, Tree<Long> treeNode) {
                                treeNode.setName(menu.getName());
                                treeNode.setId(menu.getId());
                                treeNode.setParentId(menu.getParentId());
                                treeNode.setWeight(menu.getOrderNum());
                                MetaVO metaVO = new MetaVO(menu.getName(), menu.getIcon(),
                                        StrUtil.equals("1", menu.getIsCache()), menu.getPath());
                                treeNode.put("meta", JSONUtil.parseObj(metaVO).toString());
                            }
                        });
                        // TODO: 2024/3/1   integerTree convert RouterVO
                    }
                }
            }
        }

        return null;
    }
}
