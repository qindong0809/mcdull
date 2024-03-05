package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
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
import java.util.*;
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
    public Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Integer, List<Integer>> menuListMap = roleMenuService.getMenuIdListMap(roleIdList);
            return baseRepository.menuCodeListMap(menuListMap);
        }
        return MapUtil.empty();
    }

    @Override
    public List<String> getAllCodeList() {
        return baseRepository.allCodeList();
    }

    @Override
    public List<RouterVO> tree(Integer userId) {

        Map<Integer, List<Integer>> roleIdListMap = userRoleService.getRoleIdListMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(roleIdListMap)) {
            List<Integer> roleIdList = roleIdListMap.get(userId);
            if (CollUtil.isNotEmpty(roleIdList)) {
                Map<Integer, List<Integer>> menuIdListMap = roleMenuService.getMenuIdListMap(roleIdList);
                if (MapUtil.isNotEmpty(menuIdListMap)) {
                    Set<Integer> idSet = menuIdListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
                    if (CollUtil.isNotEmpty(idSet)) {
                        List<MenuDO> menuList = baseRepository.list(idSet);

                        List<Tree<Integer>> integerTree = TreeUtil.build(menuList, 0,
                                (menu, treeNode) -> {
                            treeNode.setName(StrUtil.upperFirst(menu.getPath()));
                            treeNode.put("path", this.getRouterPath(menu));
                            treeNode.setId(menu.getId());
                            treeNode.setParentId(menu.getParentId());
                            treeNode.setWeight(menu.getOrderNum());
                            MetaVO metaVO = new MetaVO(menu.getName(), menu.getIcon(),
                                    StrUtil.equals("1", menu.getIsCache()), menu.getPath());
                            treeNode.put("meta", JSONUtil.parseObj(metaVO).toString());
                            treeNode.put("component", this.getComponent(menu));
                            treeNode.put("query", menu.getQuery());
                            treeNode.put("hidden", "1".equals(menu.getVisible()));
                        });
                        return this.convert(integerTree);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    public String getComponent(MenuDO menu) {
        String component = "Layout";
        if (StrUtil.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StrUtil.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = "InnerLink";
        } else if (StrUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = "ParentView";
        }
        return component;
    }

    public boolean isParentView(MenuDO menu) {
        return menu.getParentId().intValue() != 0 && "M".equals(menu.getMenuType());
    }

    public boolean isInnerLink(MenuDO menu) {
        return menu.getIsFrame().equals("1");
    }

    public String getRouterPath(MenuDO menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && "M".equals(menu.getMenuType())
                && "1".equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    public boolean isMenuFrame(MenuDO menu) {
        return menu.getParentId().intValue() == 0 && "M".equals(menu.getMenuType())
                && menu.getIsFrame().equals("1");
    }


    public List<RouterVO> convert(List<Tree<Integer>> treeList) {
        if (CollUtil.isEmpty(treeList)) {
            return Collections.emptyList();
        }
        List<RouterVO> list = new ArrayList<>();
        for (Tree<Integer> tree : treeList) {
            RouterVO vo = this.convert(tree);
            if (ObjUtil.isNotNull(vo)) {
                list.add(vo);
            }
        }
        return list;
    }

    private RouterVO convert(Tree<Integer> tree) {
        if (ObjUtil.isNull(tree)) {
            return null;
        }
        RouterVO routerVO = new RouterVO();
        routerVO.setName(String.valueOf(tree.getName()));
        routerVO.setPath(Convert.toStr(tree.get("path")));
        routerVO.setHidden(Convert.toBool(tree.get("hidden"), false));
        routerVO.setComponent(Convert.toStr(tree.get("component")));
        routerVO.setQuery(Convert.toStr(tree.get("query")));
        String meta = Convert.toStr(tree.get("meta"));
        if (StrUtil.isNotBlank(meta)) {
            MetaVO metaVO = JSONUtil.toBean(meta, MetaVO.class);
            routerVO.setMeta(metaVO);
        }
        List<Tree<Integer>> children = tree.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            List<RouterVO> childVOList = new ArrayList<>();
            for (Tree<Integer> childTree : children) {
                RouterVO childVO = this.convert(childTree);
                if (ObjUtil.isNotNull(childVO)) {
                    childVOList.add(childVO);
                }
            }
            routerVO.setChildren(childVOList);
            routerVO.setAlwaysShow(true);
            routerVO.setRedirect("noRedirect");

        }
        return routerVO;
    }
}
