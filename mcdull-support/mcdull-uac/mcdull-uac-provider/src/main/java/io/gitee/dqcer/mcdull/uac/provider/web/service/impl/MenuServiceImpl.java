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
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.MenuConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
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
    public Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Integer, List<Integer>> menuListMap = roleMenuService.getMenuIdListMap(roleIdList);
            return baseRepository.menuCodeListMap(menuListMap);
        }
        return MapUtil.empty();
    }

    @Override
    public Map<Integer, List<MenuEntity>> getMenuListMap(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Integer, List<Integer>> menuListMap = roleMenuService.getMenuIdListMap(roleIdList);
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
    public void insert(MenuAddDTO dto) {
        Integer parentId = dto.getParentId();
        List<MenuEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getMenuName().equals(dto.getMenuName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        MenuEntity menu = this.convertToEntity(dto);
        baseRepository.save(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(MenuUpdateDTO dto) {
        Integer id = dto.getMenuId();
        MenuEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        Integer parentId = dto.getParentId();
        List<MenuEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream()
                    .anyMatch(i -> (!i.getId().equals(id)) && i.getMenuName().equals(dto.getMenuName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        MenuEntity menu = this.setUpdateField(dto, entity);
        baseRepository.updateById(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> menuIdList) {
        List<MenuEntity> entityList = baseRepository.listByIds(menuIdList);
        if (entityList.size() != menuIdList.size()) {
            this.throwDataNotExistException(menuIdList);
        }
        baseRepository.removeBatchByIds(menuIdList);
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
    public List<Integer> roleMenuIdList(Integer roleId) {
        Map<Integer, List<Integer>> menuIdListMap = roleMenuService.getMenuIdListMap(ListUtil.of(roleId));
        if (MapUtil.isNotEmpty(menuIdListMap)) {
            List<Integer> list = menuIdListMap.get(roleId);
            if (CollUtil.isNotEmpty(list)) {
                List<MenuEntity> menuList = baseRepository.listByIds(list);
                if (CollUtil.isNotEmpty(menuList)) {
                    return menuList.stream().map(IdEntity::getId).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    private RoleMenuVO convertToRoleMenuVO(MenuEntity menu) {
        RoleMenuVO roleMenuVO = new RoleMenuVO();
        roleMenuVO.setId(menu.getId());
        return roleMenuVO;
    }

    private MenuEntity setUpdateField(MenuUpdateDTO dto, MenuEntity menuEntity) {
        menuEntity.setMenuName(dto.getMenuName());
        menuEntity.setMenuType(dto.getMenuType());
        menuEntity.setParentId(dto.getParentId());
        menuEntity.setSort(dto.getSort());
        menuEntity.setPath(dto.getPath());
        menuEntity.setComponent(dto.getComponent());
        menuEntity.setPermsType(dto.getPermsType());
        menuEntity.setApiPerms(dto.getApiPerms());
        menuEntity.setWebPerms(dto.getWebPerms());
        menuEntity.setIcon(dto.getIcon());
        menuEntity.setContextMenuId(dto.getContextMenuId());
        menuEntity.setFrameFlag(dto.getFrameFlag());
        menuEntity.setFrameUrl(dto.getFrameUrl());
        menuEntity.setCacheFlag(dto.getCacheFlag());
        menuEntity.setVisibleFlag(dto.getVisibleFlag());
        return menuEntity;
    }

    private MenuEntity convertToEntity(MenuAddDTO dto) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setMenuName(dto.getMenuName());
        menuEntity.setMenuType(dto.getMenuType());
        menuEntity.setParentId(dto.getParentId());
        menuEntity.setSort(dto.getSort());
        menuEntity.setPath(dto.getPath());
        menuEntity.setComponent(dto.getComponent());
        menuEntity.setPermsType(dto.getPermsType());
        menuEntity.setApiPerms(dto.getApiPerms());
        menuEntity.setWebPerms(dto.getWebPerms());
        menuEntity.setIcon(dto.getIcon());
        menuEntity.setContextMenuId(dto.getContextMenuId());
        menuEntity.setFrameFlag(dto.getFrameFlag());
        menuEntity.setFrameUrl(dto.getFrameUrl());
        menuEntity.setCacheFlag(dto.getCacheFlag());
        menuEntity.setVisibleFlag(dto.getVisibleFlag());
        return menuEntity;

    }

    private List<Tree<Integer>> getTrees(List<MenuEntity> menuList) {
        return TreeUtil.build(menuList, 0,
                (menu, treeNode) -> {
                    treeNode.setName(StrUtil.upperFirst(menu.getPath()));
                    treeNode.setId(menu.getId());
                    treeNode.setParentId(menu.getParentId());
                    treeNode.put("menuType", menu.getMenuType());
                    treeNode.put("contextMenuId", menu.getContextMenuId());
                    treeNode.setName(menu.getMenuName());
        });
    }


    public List<MenuSimpleTreeVO> convertSimpleRouter(List<Tree<Integer>> treeList) {
        if (CollUtil.isEmpty(treeList)) {
            return Collections.emptyList();
        }
        List<MenuSimpleTreeVO> list = new ArrayList<>();
        for (Tree<Integer> tree : treeList) {
            MenuSimpleTreeVO vo = this.convertSimpleTree(tree);
            if (ObjUtil.isNotNull(vo)) {
                list.add(vo);
            }
        }
        return list;
    }

    private MenuSimpleTreeVO convertSimpleTree(Tree<Integer> tree) {
        if (ObjUtil.isNull(tree)) {
            return null;
        }
        MenuSimpleTreeVO routerVO = new MenuSimpleTreeVO();
        routerVO.setMenuId(Convert.toInt(tree.getId()));
        routerVO.setParentId(Convert.toInt(tree.getParentId()));
        routerVO.setMenuName(String.valueOf(tree.getName()));
        routerVO.setMenuType(Convert.toInt(tree.get("menuType")));
        routerVO.setContextMenuId(Convert.toInt(tree.get("contextMenuId")));
        List<Tree<Integer>> children = tree.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            List<MenuSimpleTreeVO> childVOList = new ArrayList<>();

            for (Tree<Integer> childTree : children) {
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
    public List<MenuVO> getList(Integer userId, boolean administratorFlag) {
        Set<MenuVO> list = new HashSet<>();
        if (administratorFlag) {
            baseRepository.allList().forEach(menuEntity -> {
                MenuVO menuVO = MenuConvert.toVO(menuEntity);
                list.add(menuVO);
            });
            return new ArrayList<>(list);
        }
        Map<Integer, List<RoleEntity>> roleListMap = roleService.getRoleMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(roleListMap)) {
            List<Integer> roleIdList = roleListMap.values().stream()
                    .flatMap(roleList -> roleList.stream().map(IdEntity::getId)).collect(Collectors.toList());
            Map<Integer, List<MenuEntity>> menuListMap = this.getMenuListMap(roleIdList);
            if (MapUtil.isNotEmpty(menuListMap)) {
                for (Map.Entry<Integer, List<MenuEntity>> entry : menuListMap.entrySet()) {
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
    public RoleMenuTreeVO getTreeRoleId(Integer roleId) {
        RoleVO vo = roleService.get(roleId);
        if (ObjUtil.isNotNull(vo)) {
            RoleMenuTreeVO treeVO = new RoleMenuTreeVO();
            treeVO.setRoleId(Convert.toInt(roleId));
            List<MenuEntity> entityList = baseRepository.allList();
            if (CollUtil.isNotEmpty(entityList)) {
                List<Tree<Integer>> integerTree = this.getTrees(entityList);
                List<MenuSimpleTreeVO> menuList = this.convertSimpleRouter(integerTree);
                treeVO.setMenuTreeList(menuList);
            }
            Map<Integer, List<Integer>> menuIdListMap = roleMenuService.getMenuIdListMap(ListUtil.of(roleId));
            if (MapUtil.isNotEmpty(menuIdListMap)) {
                List<Integer> mendIdList = menuIdListMap.get(roleId);
                if (CollUtil.isNotEmpty(mendIdList)) {
                    List<Integer> idList = mendIdList.stream().map(Convert::toInt).collect(Collectors.toList());
                    treeVO.setSelectedMenuId(idList);
                }
            }
            return treeVO;
        }

        return null;
    }

    @Override
    public List<MenuTreeVO> queryMenuTree(Boolean onlyMenu) {
        List<MenuEntity> list = baseRepository.listOnlyMenu(onlyMenu);
        List<Tree<Integer>> integerTree = this.getTrees(list);
        return this.convertMenuTreeVO(integerTree);
    }

    public List<MenuTreeVO> convertMenuTreeVO(List<Tree<Integer>> treeList) {
        if (CollUtil.isEmpty(treeList)) {
            return Collections.emptyList();
        }
        List<MenuTreeVO> list = new ArrayList<>();
        for (Tree<Integer> tree : treeList) {
            MenuTreeVO vo = this.convertTreeVO(tree);
            if (ObjUtil.isNotNull(vo)) {
                list.add(vo);
            }
        }
        return list;
    }

    private MenuTreeVO convertTreeVO(Tree<Integer> tree) {
        if (ObjUtil.isNull(tree)) {
            return null;
        }
        MenuTreeVO routerVO = new MenuTreeVO();
        routerVO.setMenuId(Convert.toInt(tree.getId()));
        routerVO.setParentId(Convert.toInt(tree.getParentId()));
        routerVO.setMenuName(String.valueOf(tree.getName()));
        routerVO.setMenuType(Convert.toInt(tree.get("menuType")));
        routerVO.setContextMenuId(Convert.toInt(tree.get("contextMenuId")));
        List<Tree<Integer>> children = tree.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            List<MenuTreeVO> childVOList = new ArrayList<>();

            for (Tree<Integer> childTree : children) {
                MenuTreeVO childVO = this.convertTreeVO(childTree);
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
}
