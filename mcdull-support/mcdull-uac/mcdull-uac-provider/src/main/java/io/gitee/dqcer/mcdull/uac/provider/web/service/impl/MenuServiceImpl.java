package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.MenuAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.MenuConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuBaseForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IMenuManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
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

    @Resource
    private IAuditManager auditManager;

    @Resource
    private IMenuManager menuManager;

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
        auditManager.saveByAddEnum(dto.getMenuName(), menu.getId(), this.buildAuditLog(menu));
    }

    private Audit buildAuditLog(MenuEntity menu) {
        MenuAudit audit = new MenuAudit();
        audit.setMenuName(menu.getMenuName());
        audit.setSort(menu.getSort());
        audit.setMenuTypeName(IEnum.getTextByCode(MenuTypeEnum.class, menu.getMenuType()));
        Integer parentId = menu.getParentId();
        if (ObjUtil.isNotNull(parentId)) {
            MenuEntity menuEntity = baseRepository.getById(parentId);
            if (ObjUtil.isNotNull(menuEntity)) {
                audit.setParentName(menuEntity.getMenuName());
            }
        }
        audit.setPath(menu.getPath());
        audit.setComponent(menu.getComponent());
        audit.setApiPerms(menu.getApiPerms());
        audit.setWebPerms(menu.getWebPerms());
        audit.setIcon(menu.getIcon());
        audit.setContextMenuId(this.convertContextMenuId(menu.getContextMenuId()));
        audit.setFrameFlag(BooleanUtil.toStringYesNo(menu.getFrameFlag()));
        audit.setFrameUrl(menu.getFrameUrl());
        audit.setCacheFlag(BooleanUtil.toStringYesNo(menu.getCacheFlag()));
        audit.setVisibleFlag(BooleanUtil.toStringYesNo(menu.getVisibleFlag()));
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(MenuUpdateDTO dto) {
        Integer id = dto.getMenuId();
        MenuEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        MenuEntity oldEntity = ObjUtil.cloneByStream(entity);
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
        auditManager.saveByUpdateEnum(dto.getMenuName(), id,
                this.buildAuditLog(oldEntity), this.buildAuditLog(menu));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> menuIdList) {
        List<MenuEntity> entityList = baseRepository.listByIds(menuIdList);
        if (entityList.size() != menuIdList.size()) {
            this.throwDataNotExistException(menuIdList);
        }
        baseRepository.removeByIds(menuIdList);
        for (MenuEntity entity : entityList) {
            auditManager.saveByDeleteEnum(entity.getMenuName(), entity.getId(), null);
        }
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
    public boolean exportData(MenuListDTO dto) {
        List<MenuVO> list = CollUtil.emptyIfNull(this.list(dto));
        List<Integer> parentList = list.stream().map(MenuVO::getParentId)
                .distinct().collect(Collectors.toList());
        Map<Integer, MenuEntity> parentMap = baseRepository.listByIds(parentList).stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        List<Pair<String, String>> pairList = this.getTitleList();
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
            map.put("contextMenu", this.convertContextMenuId(vo.getContextMenuId()));
            map.put("frameFlag", BooleanUtil.toStringYesNo(vo.getFrameFlag()));
            map.put("frameUrl", vo.getFrameUrl());
            map.put("cacheFlag", BooleanUtil.toStringYesNo(vo.getCacheFlag()));
            map.put("visibleFlag", BooleanUtil.toStringYesNo(vo.getVisibleFlag()));
            mapList.add(map);
        }
        commonManager.exportExcel("菜单列表", StrUtil.EMPTY, pairList, mapList);
        return true;
    }

    @Override
    public List<LabelValueVO<String, String>> getDropdownOptions() {
        return menuManager.getNameCodeList();
    }

    private List<String> getMenuNameByPermissionCode(String permissionCode) {
        if (StrUtil.isNotBlank(permissionCode)) {
            List<String> nameList = new ArrayList<>();
            List<MenuEntity> all = baseRepository.all();
            MenuEntity permissionEntity = all.stream()
                    .filter(menuEntity -> StrUtil.equals(permissionCode, menuEntity.getApiPerms())).findFirst().orElse(null);
            if (ObjUtil.isNotNull(permissionEntity)) {
                this.searchMenuName(all, permissionEntity.getParentId(), nameList);
                return CollUtil.reverse(nameList);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getCurrentMenuName() {
        UnifySession<?> session = UserContextHolder.getSession();
        String permissionCode = session.getPermissionCode();
        if (StrUtil.isBlank(permissionCode)) {
            throw new BusinessException(I18nConstants.MISSING_PARAMETER);
        }
        List<String> menuNameList = this.getMenuNameByPermissionCode(permissionCode);
        if (CollUtil.isEmpty(menuNameList)) {
            LogHelp.error(log, "menuName is not exist. permissionCode: {}", permissionCode);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        return menuNameList;
    }

    private void searchMenuName(List<MenuEntity> all, Integer id, List<String> nameList) {
        MenuEntity menu = all.stream().
                filter(menuEntity -> ObjUtil.equal(id, menuEntity.getId())).findFirst().orElse(null);
        if (ObjUtil.isNotNull(menu)) {
//            if (ObjUtil.equal(MenuTypeEnum.MENU.getCode(), menu.getMenuType())) {
                nameList.add(menu.getMenuName());
//            }
            this.searchMenuName(all, menu.getParentId(), nameList);
        }
    }

    private List<Pair<String, String>> getTitleList() {
        List<Pair<String, String>> pairList = new ArrayList<>();
        pairList.add(Pair.of("菜单名称", "menuName"));
        pairList.add(Pair.of("菜单类型", "menuType"));
        pairList.add(Pair.of("父级", "parentName"));
        pairList.add(Pair.of("排序", "sort"));
        pairList.add(Pair.of("路由地址", "path"));
        pairList.add(Pair.of("组件路径", "component"));
        pairList.add(Pair.of("api权限", "apiPerms"));
        pairList.add(Pair.of("web权限", "webPerms"));
        pairList.add(Pair.of("图标", "icon"));
        pairList.add(Pair.of("是否为上下文菜单", "contextMenu"));
        pairList.add(Pair.of("是否为frame", "frameFlag"));
        pairList.add(Pair.of("frame地址", "frameUrl"));
        pairList.add(Pair.of("是否缓存", "cacheFlag"));
        pairList.add(Pair.of("是否可见", "visibleFlag"));
        return pairList;
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
        routerVO.setMenuId(Convert.toInt(tree.getId(), 0));
        routerVO.setParentId(Convert.toInt(tree.getParentId()));
        routerVO.setMenuName(String.valueOf(tree.getName()));
        routerVO.setMenuType(Convert.toInt(tree.get("menuType")));
        routerVO.setContextMenuId(Convert.toInt(tree.get("contextMenuId")));
        return tree.getChildren();
    }

    private String convertContextMenuId(Integer menuId) {
        if (ObjUtil.isNull(menuId)) {
            return StrUtil.EMPTY;
        }
        return menuId.toString();
    }
}
