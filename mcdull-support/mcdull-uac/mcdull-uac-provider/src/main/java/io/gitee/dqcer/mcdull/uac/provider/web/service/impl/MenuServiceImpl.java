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
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.MenuConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
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
    private IRoleService roleService;

    @Override
    public Map<Long, List<String>> getMenuCodeListMap(List<Long> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Long, List<Long>> menuListMap = roleMenuService.getMenuIdListMap(roleIdList);
            return baseRepository.menuCodeListMap(menuListMap);
        }
        return MapUtil.empty();
    }

    @Override
    public Map<Long, List<MenuEntity>> getMenuListMap(List<Long> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Long, List<Long>> menuListMap = roleMenuService.getMenuIdListMap(roleIdList);
            return baseRepository.getMenuListMap(menuListMap);
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
        List<MenuEntity> menuList = baseRepository.allAndButton();
        if (CollUtil.isNotEmpty(menuList)) {
            for (MenuEntity menu : menuList) {
                MenuVO vo = MenuConvert.toVO(menu);
                list.add(vo);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(MenuInsertDTO dto) {
        Long parentId = dto.getParentId();

        List<MenuEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getMenuName().equals(dto.getMenuName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        MenuEntity menu = this.convertToEntity(dto);
        return baseRepository.save(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Long id, MenuUpdateDTO dto) {
        Long parentId = dto.getParentId();
        List<MenuEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream()
                    .anyMatch(i -> (!i.getId().equals(id)) && i.getMenuName().equals(dto.getMenuName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        MenuEntity menu = this.convertToEntity(dto);
        menu.setId(id);
        return baseRepository.updateById(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    @Override
    public List<RoleMenuVO> roleMenuList() {
        List<RoleMenuVO> voList = new ArrayList<>();
        List<MenuEntity> menuList = baseRepository.allAndButton();
        if (CollUtil.isNotEmpty(menuList)) {
            for (MenuEntity menu : menuList) {
                RoleMenuVO vo = this.convertToRoleMenuVO(menu);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public List<Long> roleMenuIdList(Long roleId) {
        Map<Long, List<Long>> menuIdListMap = roleMenuService.getMenuIdListMap(ListUtil.of(roleId));
        if (MapUtil.isNotEmpty(menuIdListMap)) {
            List<Long> list = menuIdListMap.get(roleId);
            if (CollUtil.isNotEmpty(list)) {
                List<MenuEntity> menuList = baseRepository.listByIds(list);
                if (CollUtil.isNotEmpty(menuList)) {
                    return menuList.stream().map(IdEntity::getId).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<PermissionRouterVO> getPermissionRouter() {
        List<MenuEntity> menuList = baseRepository.all();
        List<Tree<Long>> integerTree = this.getTrees(menuList);
        return this.convertPermissionRouter(integerTree);
    }

    @Override
    public List<PermissionRouterVO> getPermissionRouterByRole(Long roleId) {
        return this.getRouter(ListUtil.of(roleId));
    }

    private RoleMenuVO convertToRoleMenuVO(MenuEntity menu) {
        RoleMenuVO roleMenuVO = new RoleMenuVO();
        roleMenuVO.setId(menu.getId());
        return roleMenuVO;
    }

    private MenuEntity convertToEntity(MenuUpdateDTO dto) {
        MenuEntity entity = new MenuEntity();
        entity.setMenuName(dto.getMenuName());
        entity.setMenuType(dto.getMenuType());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        entity.setPath(dto.getPath());
        entity.setComponent(dto.getComponent());
        entity.setPermsType(dto.getPermsType());
        entity.setApiPerms(dto.getApiPerms());
        entity.setWebPerms(dto.getWebPerms());
        entity.setIcon(dto.getIcon());
        entity.setContextMenuId(dto.getContextMenuId());
        entity.setFrameFlag(dto.getFrameFlag());
        entity.setFrameUrl(dto.getFrameUrl());
        entity.setCacheFlag(dto.getCacheFlag());
        entity.setVisibleFlag(dto.getVisibleFlag());
        return entity;
    }

    private MenuEntity convertToEntity(MenuInsertDTO dto) {
        MenuEntity entity = new MenuEntity();
        entity.setMenuName(dto.getMenuName());
        entity.setMenuType(dto.getMenuType());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        entity.setPath(dto.getPath());
        entity.setComponent(dto.getComponent());
        entity.setPermsType(dto.getPermsType());
        entity.setApiPerms(dto.getApiPerms());
        entity.setWebPerms(dto.getWebPerms());
        entity.setIcon(dto.getIcon());
        entity.setContextMenuId(dto.getContextMenuId());
        entity.setFrameFlag(dto.getFrameFlag());
        entity.setFrameUrl(dto.getFrameUrl());
        entity.setCacheFlag(dto.getCacheFlag());
        entity.setVisibleFlag(dto.getVisibleFlag());
        return entity;
    }

    private List<PermissionRouterVO> getRouter(List<Long> roleIdList) {
        Map<Long, List<Long>> menuIdListMap = roleMenuService.getMenuIdListMap(roleIdList);
        if (MapUtil.isNotEmpty(menuIdListMap)) {
            Set<Long> idSet = menuIdListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(idSet)) {
                List<MenuEntity> menuList = baseRepository.list(idSet);
                List<Tree<Long>> integerTree = this.getTrees(menuList);
                return this.convertPermissionRouter(integerTree);
            }
        }
        return Collections.emptyList();
    }

    private List<Tree<Long>> getTrees(List<MenuEntity> menuList) {
        Map<Long, List<RoleEntity>> roleMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(menuList)) {
            List<Long> menuIdList = menuList.stream().map(IdEntity::getId).collect(Collectors.toList());
            roleMap = roleService.getRoleMapByMenuId(menuIdList);
        }
        Map<Long, List<RoleEntity>> finalRoleMap = roleMap;
        return TreeUtil.build(menuList, 0L,
                (menu, treeNode) -> {
                    treeNode.setName(StrUtil.upperFirst(menu.getPath()));
                    treeNode.setId(menu.getId());
                    treeNode.setParentId(menu.getParentId());
                    treeNode.put("menuType", menu.getMenuType());
                    treeNode.put("contextMenuId", menu.getContextMenuId());
                    treeNode.setName(menu.getMenuName());
        });
    }


    public List<PermissionRouterVO> convertPermissionRouter(List<Tree<Long>> treeList) {
        if (CollUtil.isEmpty(treeList)) {
            return Collections.emptyList();
        }
        List<PermissionRouterVO> list = new ArrayList<>();
        for (Tree<Long> tree : treeList) {
            PermissionRouterVO vo = this.convertPermission(tree);
            if (ObjUtil.isNotNull(vo)) {
                list.add(vo);
            }
        }
        return list;
    }

    public List<MenuSimpleTreeVO> convertSimpleRouter(List<Tree<Long>> treeList) {
        if (CollUtil.isEmpty(treeList)) {
            return Collections.emptyList();
        }
        List<MenuSimpleTreeVO> list = new ArrayList<>();
        for (Tree<Long> tree : treeList) {
            MenuSimpleTreeVO vo = this.convertSimpleTree(tree);
            if (ObjUtil.isNotNull(vo)) {
                list.add(vo);
            }
        }
        return list;
    }

    private MenuSimpleTreeVO convertSimpleTree(Tree<Long> tree) {
        if (ObjUtil.isNull(tree)) {
            return null;
        }
        MenuSimpleTreeVO routerVO = new MenuSimpleTreeVO();
        routerVO.setMenuId(Convert.toInt(tree.getId()));
        routerVO.setParentId(Convert.toInt(tree.getParentId()));
        routerVO.setMenuName(String.valueOf(tree.getName()));
        routerVO.setMenuType(Convert.toInt(tree.get("menuType")));
        routerVO.setContextMenuId(Convert.toInt(tree.get("contextMenuId")));
        List<Tree<Long>> children = tree.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            List<MenuSimpleTreeVO> childVOList = new ArrayList<>();

            for (Tree<Long> childTree : children) {
                MenuSimpleTreeVO childVO = this.convertSimpleTree(childTree);
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

    private PermissionRouterVO convertPermission(Tree<Long> tree) {
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
        List<Tree<Long>> children = tree.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            List<PermissionRouterVO> childVOList = new ArrayList<>();

            for (Tree<Long> childTree : children) {
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


    @Override
    public List<MenuVO> getList(Long userId, boolean administratorFlag) {
        Set<MenuVO> list = new HashSet<>();
        if (administratorFlag) {
            baseRepository.allList().forEach(menuEntity -> {
                MenuVO menuVO = MenuConvert.toVO(menuEntity);
                list.add(menuVO);
            });
            return new ArrayList<>(list);
        }
        Map<Long, List<RoleEntity>> roleListMap = roleService.getRoleMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(roleListMap)) {
            List<Long> roleIdList = roleListMap.values().stream()
                    .flatMap(roleList -> roleList.stream().map(IdEntity::getId)).collect(Collectors.toList());
            Map<Long, List<MenuEntity>> menuListMap = this.getMenuListMap(roleIdList);
            if (MapUtil.isNotEmpty(menuListMap)) {
                for (Map.Entry<Long, List<MenuEntity>> entry : menuListMap.entrySet()) {
                    List<MenuEntity> menuList = entry.getValue();
                    if (CollUtil.isNotEmpty(menuList)) {
                        for (MenuEntity menuEntity : menuList) {
                            MenuVO menuVO = MenuConvert.toVO(menuEntity);
                            list.add(menuVO);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(list);
    }

    @Override
    public RoleMenuTreeVO getTreeRoleId(Long roleId) {
        RoleVO vo = roleService.get(roleId);
        if (ObjUtil.isNotNull(vo)) {
            RoleMenuTreeVO treeVO = new RoleMenuTreeVO();
            treeVO.setRoleId(Convert.toInt(roleId));
            List<MenuEntity> entityList = baseRepository.allList();
            if (CollUtil.isNotEmpty(entityList)) {
                List<Tree<Long>> integerTree = this.getTrees(entityList);
                List<MenuSimpleTreeVO> menuList = this.convertSimpleRouter(integerTree);
                treeVO.setMenuTreeList(menuList);
            }
            Map<Long, List<Long>> menuIdListMap = roleMenuService.getMenuIdListMap(ListUtil.of(roleId));
            if (MapUtil.isNotEmpty(menuIdListMap)) {
                List<Long> mendIdList = menuIdListMap.get(roleId);
                if (CollUtil.isNotEmpty(mendIdList)) {
                    List<Integer> idList = mendIdList.stream().map(Convert::toInt).collect(Collectors.toList());
                    treeVO.setSelectedMenuId(idList);
                }
            }
            return treeVO;
        }

        return null;
    }
}
