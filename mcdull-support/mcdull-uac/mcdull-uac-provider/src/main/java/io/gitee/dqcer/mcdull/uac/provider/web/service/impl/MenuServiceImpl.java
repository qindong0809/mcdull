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
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<MenuVO> list(MenuListDTO dto) {
        List<MenuVO> list = new ArrayList<>();
        List<MenuDO> menuList = baseRepository.allAndButton();
        if (CollUtil.isNotEmpty(menuList)) {
            for (MenuDO menu : menuList) {
                MenuVO vo = this.convertToVO(menu);
                list.add(vo);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(MenuInsertDTO dto) {
        Integer parentId = dto.getParentId();

        List<MenuDO> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        MenuDO menu = this.convertToEntity(dto);
        return baseRepository.save(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, MenuUpdateDTO dto) {
        Integer parentId = dto.getParentId();
        List<MenuDO> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream()
                    .anyMatch(i -> (!i.getId().equals(id)) && i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        MenuDO menu = this.convertToEntity(dto);
        menu.setId(id);
        return baseRepository.updateById(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    @Override
    public List<RoleMenuVO> roleMenuList() {
        List<RoleMenuVO> voList = new ArrayList<>();
        List<MenuDO> menuList = baseRepository.allAndButton();
        if (CollUtil.isNotEmpty(menuList)) {
            for (MenuDO menu : menuList) {
                RoleMenuVO vo = this.convertToRoleMenuVO(menu);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public List<Integer> roleMenuIdList(Integer roleId) {
        Map<Integer, List<Integer>> menuIdListMap = roleMenuService.getMenuIdListMap(ListUtil.of(roleId));
        if (MapUtil.isNotEmpty(menuIdListMap)) {
            List<Integer> list = menuIdListMap.get(roleId);
            if (CollUtil.isNotEmpty(list)) {
                List<MenuDO> menuList = baseRepository.listByIds(list);
                if (CollUtil.isNotEmpty(menuList)) {
                    return menuList.stream().map(IdDO::getId).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<PermissionRouterVO> getPermissionRouter() {
        List<MenuDO> menuList = baseRepository.all();
        List<Tree<Integer>> integerTree = this.getTrees(menuList);
        return this.convertPermissionRouter(integerTree);
    }

    @Override
    public List<PermissionRouterVO> getPermissionRouterByRole(Integer roleId) {
        return this.getRouter(ListUtil.of(roleId));
    }

    private RoleMenuVO convertToRoleMenuVO(MenuDO menu) {
        RoleMenuVO roleMenuVO = new RoleMenuVO();
        roleMenuVO.setId(menu.getId());
        roleMenuVO.setMenuType(menu.getMenuType());
        roleMenuVO.setParentId(menu.getParentId());
        roleMenuVO.setTitle(menu.getTitle());
        return roleMenuVO;
    }

    private MenuDO convertToEntity(MenuUpdateDTO dto) {
        MenuDO menuDO = new MenuDO();
        menuDO.setMenuType(dto.getMenuType());
        menuDO.setParentId(dto.getParentId());
        menuDO.setTitle(dto.getTitle());
        menuDO.setName(dto.getName());
        menuDO.setPath(dto.getPath());
        menuDO.setComponent(dto.getComponent());
        menuDO.setRankOrder(dto.getRank());
        menuDO.setRedirect(dto.getRedirect());
        menuDO.setIcon(dto.getIcon());
        menuDO.setExtraIcon(dto.getExtraIcon());
        menuDO.setEnterTransition(dto.getEnterTransition());
        menuDO.setLeaveTransition(dto.getLeaveTransition());
        menuDO.setActivePath(dto.getActivePath());
        menuDO.setAuths(dto.getAuths());
        menuDO.setFrameSrc(dto.getFrameSrc());
        menuDO.setFrameLoading(dto.getFrameLoading());
        menuDO.setKeepAlive(dto.getKeepAlive());
        menuDO.setHiddenTag(dto.getHiddenTag());
        menuDO.setShowLink(dto.getShowLink());
        menuDO.setShowParent(dto.getShowParent());
        return menuDO;
    }

    private MenuDO convertToEntity(MenuInsertDTO dto) {
        MenuDO menuDO = new MenuDO();
        menuDO.setMenuType(dto.getMenuType());
        menuDO.setParentId(dto.getParentId());
        menuDO.setTitle(dto.getTitle());
        menuDO.setName(dto.getName());
        menuDO.setPath(dto.getPath());
        menuDO.setComponent(dto.getComponent());
        menuDO.setRankOrder(dto.getRank());
        menuDO.setRedirect(dto.getRedirect());
        menuDO.setIcon(dto.getIcon());
        menuDO.setExtraIcon(dto.getExtraIcon());
        menuDO.setEnterTransition(dto.getEnterTransition());
        menuDO.setLeaveTransition(dto.getLeaveTransition());
        menuDO.setActivePath(dto.getActivePath());
        menuDO.setAuths(dto.getAuths());
        menuDO.setFrameSrc(dto.getFrameSrc());
        menuDO.setFrameLoading(dto.getFrameLoading());
        menuDO.setKeepAlive(dto.getKeepAlive());
        menuDO.setHiddenTag(dto.getHiddenTag());
        menuDO.setShowLink(dto.getShowLink());
        menuDO.setShowParent(dto.getShowParent());
        return menuDO;
    }

    private MenuVO convertToVO(MenuDO menu) {
        MenuVO menuVO = new MenuVO();
        menuVO.setId(menu.getId());
        menuVO.setMenuType(menu.getMenuType());
        menuVO.setParentId(menu.getParentId());
        menuVO.setTitle(menu.getTitle());
        menuVO.setName(menu.getName());
        menuVO.setPath(menu.getPath());
        menuVO.setComponent(menu.getComponent());
        menuVO.setRank(menu.getRankOrder());
        menuVO.setRedirect(menu.getRedirect());
        menuVO.setIcon(menu.getIcon());
        menuVO.setExtraIcon(menu.getExtraIcon());
        menuVO.setEnterTransition(menu.getEnterTransition());
        menuVO.setLeaveTransition(menu.getLeaveTransition());
        menuVO.setActivePath(menu.getActivePath());
        menuVO.setAuths(menu.getAuths());
        menuVO.setFrameSrc(menu.getFrameSrc());
        menuVO.setFrameLoading(menu.getFrameLoading());
        menuVO.setKeepAlive(menu.getKeepAlive());
        menuVO.setHiddenTag(menu.getHiddenTag());
        menuVO.setShowLink(menu.getShowLink());
        menuVO.setShowParent(menu.getShowParent());
        return menuVO;

    }


    private List<PermissionRouterVO> getRouter(List<Integer> roleIdList) {
        Map<Integer, List<Integer>> menuIdListMap = roleMenuService.getMenuIdListMap(roleIdList);
        if (MapUtil.isNotEmpty(menuIdListMap)) {
            Set<Integer> idSet = menuIdListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(idSet)) {
                List<MenuDO> menuList = baseRepository.list(idSet);
                List<Tree<Integer>> integerTree = this.getTrees(menuList);
                return this.convertPermissionRouter(integerTree);
            }
        }
        return Collections.emptyList();
    }

    private List<Tree<Integer>> getTrees(List<MenuDO> menuList) {
        return TreeUtil.build(menuList, 0,
                (menu, treeNode) -> {
            treeNode.setName(StrUtil.upperFirst(menu.getPath()));
            treeNode.setId(menu.getId());
            treeNode.setParentId(menu.getParentId());
            treeNode.put("path", menu.getPath());
            treeNode.put("component", menu.getComponent());
            treeNode.setName(menu.getName());
            treeNode.setWeight(menu.getRankOrder());
            PermissionRouterVO.MetaVO meta = new PermissionRouterVO.MetaVO();
            meta.setTitle(menu.getTitle());
            meta.setIcon(menu.getIcon());
            meta.setRank(menu.getRankOrder());
            meta.setFrameSrc(menu.getFrameSrc());
            meta.setKeepAlive(menu.getKeepAlive());
            meta.setAuths(ListUtil.of(menu.getAuths()));
            // FIXME: 2024/3/24
            meta.setRoles(ListUtil.of("admin"));
            treeNode.put("meta", JSONUtil.parseObj(meta).toString());
        });
    }


    public List<PermissionRouterVO> convertPermissionRouter(List<Tree<Integer>> treeList) {
        if (CollUtil.isEmpty(treeList)) {
            return Collections.emptyList();
        }
        List<PermissionRouterVO> list = new ArrayList<>();
        for (Tree<Integer> tree : treeList) {
            PermissionRouterVO vo = this.convertPermission(tree);
            if (ObjUtil.isNotNull(vo)) {
                list.add(vo);
            }
        }
        return list;
    }

    private PermissionRouterVO convertPermission(Tree<Integer> tree) {
        if (ObjUtil.isNull(tree)) {
            return null;
        }
        PermissionRouterVO routerVO = new PermissionRouterVO();
        routerVO.setName(String.valueOf(tree.getName()));
        routerVO.setPath(Convert.toStr(tree.get("path")));
        routerVO.setComponent(Convert.toStr(tree.get("component")));
        String meta = Convert.toStr(tree.get("meta"));
        if (StrUtil.isNotBlank(meta)) {
            PermissionRouterVO.MetaVO metaVO = JSONUtil.toBean(meta, PermissionRouterVO.MetaVO.class);
            routerVO.setMeta(metaVO);
        }
        List<Tree<Integer>> children = tree.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            List<PermissionRouterVO> childVOList = new ArrayList<>();

            for (Tree<Integer> childTree : children) {
                PermissionRouterVO childVO = this.convertPermission(childTree);
                if (ObjUtil.isNotNull(childVO)) {
                    childVOList.add(childVO);
                }
            }
            if (CollUtil.isNotEmpty(childVOList)) {
                routerVO.setChildren(childVOList);
            }
        }
        return routerVO;
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
