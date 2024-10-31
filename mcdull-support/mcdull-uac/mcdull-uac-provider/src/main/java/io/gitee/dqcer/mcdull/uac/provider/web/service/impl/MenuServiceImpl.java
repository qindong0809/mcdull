package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.MenuConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuBaseForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Menu ServiceImpl
 *
 * @author dqcer
 * @since 2024/7/25 9:57
 */

@Service
public class MenuServiceImpl
        extends BasicServiceImpl<IMenuRepository>  implements IMenuService {

    @Resource
    private IRoleMenuService roleMenuService;

    @Resource
    private IRoleService roleService;

    @Resource
    private ICommonManager commonManager;

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
        baseRepository.removeByIds(menuIdList);
    }

    private MenuEntity setUpdateField(MenuUpdateDTO dto, MenuEntity menuEntity) {
        this.setCommonField(dto, menuEntity);
        return menuEntity;
    }

    private MenuEntity convertToEntity(MenuAddDTO dto) {
        MenuEntity menuEntity = new MenuEntity();
        this.setCommonField(dto, menuEntity);
        return menuEntity;

    }

    private void setCommonField(MenuBaseForm dto, MenuEntity menuEntity) {
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

//    @Cacheable(cacheNames = GlobalConstant.CAFFEINE_CACHE, key = "#onlyMenu")
    @Override
    public List<MenuTreeVO> queryMenuTree(Boolean onlyMenu) {
        List<MenuEntity> list = baseRepository.listOnlyMenu(onlyMenu);
        List<Tree<Integer>> integerTree = this.getTrees(list);
        return this.convertMenuTreeVO(integerTree);
    }

    @Override
    public void exportData(MenuListDTO dto) {
        List<MenuVO> list = CollUtil.emptyIfNull(this.list(dto));
        List<Integer> parentList = list.stream().map(MenuVO::getParentId)
                .distinct().collect(Collectors.toList());
        Map<Integer, MenuEntity> parentMap = baseRepository.listByIds(parentList).stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        Map<String, String> titleMap = this.getTitleMap();
        List<Map<String, String>> mapList = new ArrayList<>();
        for (MenuVO vo : list) {
            Map<String, String> map = new HashMap<>();
            map.put("menuName", vo.getMenuName());
            map.put("menuType", Convert.toStr(vo.getMenuType()));
            Integer parentId = vo.getParentId();
            if (ObjUtil.isNotNull(parentId)) {
                MenuEntity menuEntity = parentMap.get(parentId);
                if (ObjUtil.isNotNull(menuEntity)) {
                    map.put("parentName", menuEntity.getMenuName());
                }
            }
            map.put("sort", Convert.toStr(vo.getSort()));
            map.put("path", StrUtil.isEmpty(vo.getPath()) ? StrUtil.EMPTY : vo.getPath());
            map.put("component", vo.getComponent());
            map.put("apiPerms", vo.getApiPerms());
            map.put("webPerms", vo.getWebPerms());
            map.put("icon", vo.getIcon());
            map.put("contextMenu", Convert.toStr(vo.getContextMenuId()));
            map.put("frameFlag", BooleanUtil.toStringYesNo(vo.getFrameFlag()));
            map.put("frameUrl", vo.getFrameUrl());
            map.put("cacheFlag", BooleanUtil.toStringYesNo(vo.getCacheFlag()));
            map.put("visibleFlag", BooleanUtil.toStringYesNo(vo.getVisibleFlag()));
            mapList.add(map);
        }
        commonManager.exportExcel("菜单列表", StrUtil.EMPTY, titleMap, mapList);
    }

    private Map<String, String> getTitleMap() {
        Map<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("菜单名称", "menuName");
        titleMap.put("菜单类型", "menuType");
        titleMap.put("父级", "parentName");
        titleMap.put("排序", "sort");
        titleMap.put("路由地址", "path");
        titleMap.put("组件路径", "component");
        titleMap.put("api权限", "apiPerms");
        titleMap.put("web权限", "webPerms");
        titleMap.put("图标", "icon");
        titleMap.put("是否为上下文菜单", "contextMenu");
        titleMap.put("是否为frame", "frameFlag");
        titleMap.put("frame地址", "frameUrl");
        titleMap.put("是否缓存", "cacheFlag");
        titleMap.put("是否可见", "visibleFlag");
        return titleMap;
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
        List<Tree<Integer>> children = this.getChildren(tree, routerVO);
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

    private List<Tree<Integer>> getChildren(Tree<Integer> tree, MenuTreeVO routerVO) {
        routerVO.setMenuId(Convert.toInt(tree.getId()));
        routerVO.setParentId(Convert.toInt(tree.getParentId()));
        routerVO.setMenuName(String.valueOf(tree.getName()));
        routerVO.setMenuType(Convert.toInt(tree.get("menuType")));
        routerVO.setContextMenuId(Convert.toInt(tree.get("contextMenuId")));
        return tree.getChildren();
    }
}
