package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.excel.ExcelUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.audit.MenuAudit;
import io.gitee.dqcer.mcdull.system.provider.model.convert.MenuConvert;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuBaseForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.*;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IMenuManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
        List<MenuVO> voList = CollUtil.emptyIfNull(this.list(dto));
        commonManager.exportExcel(voList, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<MenuVO, ?>>> getTitleList() {
        List<Pair<String, Func1<MenuVO, ?>>> list = new ArrayList<>();
        list.add(Pair.of("ID", MenuVO::getMenuId));
        list.add(Pair.of("菜单名称", MenuVO::getMenuName));
        list.add(Pair.of("菜单类型", MenuVO::getMenuType));
        list.add(Pair.of("父级", MenuVO::getParentId));
        list.add(Pair.of("排序", MenuVO::getSort));
        list.add(Pair.of("路由地址", MenuVO::getPath));
        list.add(Pair.of("组件路径", MenuVO::getComponent));
        list.add(Pair.of("权限类型", MenuVO::getPermsType));
        list.add(Pair.of("api权限", MenuVO::getApiPerms));
        list.add(Pair.of("web权限", MenuVO::getWebPerms));
        list.add(Pair.of("图标", MenuVO::getIcon));
        list.add(Pair.of("是否为上下文菜单", MenuVO::getContextMenuId));
        list.add(Pair.of("是否为frame", MenuVO::getFrameFlag));
        list.add(Pair.of("frame地址", MenuVO::getFrameUrl));
        list.add(Pair.of("是否缓存", MenuVO::getCacheFlag));
        list.add(Pair.of("是否可见", MenuVO::getVisibleFlag));
        return list;
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
        UnifySession session = UserContextHolder.getSession();
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean importMenu(MultipartFile file) throws IOException {
        List<MenuEntity> list = new ArrayList<>();
        ExcelUtil.readExcel(file.getInputStream(), () -> new AnalysisEventListener<Object>() {
            @Override
            public void invoke(Object o, AnalysisContext analysisContext) {
                List<Pair<String, Func1<MenuVO, ?>>> titleList = getTitleList();
                JSONObject jsonObject = JSONUtil.parseObj(o);
                MenuEntity entity = new MenuEntity();
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    Pair<String, Func1<MenuVO, ?>> pari = titleList.get(Convert.toInt(key));
                    if (pari != null) {
                        String fieldName = LambdaUtil.getFieldName(pari.getValue());
                        Object value = entry.getValue();
                        if (!StrUtil.equalsIgnoreCase(fieldName, "menuId")) {
                            ReflectUtil.setFieldValue(entity, fieldName, value);
                        } else {
                            entity.setId(Convert.toInt(value));
                        }
                    }
                }
                list.add(entity);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        });
        List<MenuEntity> menuList = baseRepository.allAndButton();
        List<Integer> importIdList = list.stream().map(IdEntity::getId).collect(Collectors.toList());
        List<Integer> dbIdList = menuList.stream().map(IdEntity::getId).collect(Collectors.toList());

        List<Integer> deleteIdList = dbIdList.stream().filter(i -> !importIdList.contains(i)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deleteIdList)) {
            baseRepository.removeByIds(deleteIdList);
        }
        List<Integer> insertIdList = importIdList.stream().filter(i -> !dbIdList.contains(i)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(insertIdList)) {
            for (MenuEntity entity : list) {
                if (insertIdList.contains(entity.getId())) {
                    baseRepository.save(entity);
                }
            }
        }
        // 交集
        Collection<Integer> intersection = CollUtil.intersection(importIdList, dbIdList);
        if (CollUtil.isNotEmpty(intersection)) {
            for (MenuEntity entity : menuList) {
                if (intersection.contains(entity.getId())) {
                    MenuEntity newEntity = list.stream().filter(i -> i.getId().equals(entity.getId())).findFirst().orElse(null);
                    if (ObjUtil.isNotNull(newEntity)) {
                        entity.setMenuName(newEntity.getMenuName());
                        entity.setMenuType(newEntity.getMenuType());
                        entity.setParentId(newEntity.getParentId());
                        entity.setSort(newEntity.getSort());
                        entity.setPath(newEntity.getPath());
                        entity.setComponent(newEntity.getComponent());
                        entity.setPermsType(newEntity.getPermsType());
                        entity.setApiPerms(newEntity.getApiPerms());
                        entity.setWebPerms(newEntity.getWebPerms());
                        entity.setIcon(newEntity.getIcon());
                        entity.setContextMenuId(newEntity.getContextMenuId());
                        entity.setFrameFlag(newEntity.getFrameFlag());
                        entity.setFrameUrl(newEntity.getFrameUrl());
                        entity.setCacheFlag(newEntity.getCacheFlag());
                        entity.setVisibleFlag(newEntity.getVisibleFlag());
                        baseRepository.updateById(entity);
                    }
                }
            }
        }
        return true;
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
