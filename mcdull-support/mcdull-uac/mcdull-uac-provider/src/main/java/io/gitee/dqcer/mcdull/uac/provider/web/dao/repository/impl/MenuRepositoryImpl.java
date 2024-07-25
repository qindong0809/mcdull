package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.MenuMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Menu RepositoryImpl
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Service
public class MenuRepositoryImpl
        extends ServiceImpl<MenuMapper, MenuEntity> implements IMenuRepository {


    @Override
    public List<String> allCodeList() {
        List<MenuEntity> list = this.allList();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MenuEntity::getApiPerms).collect(Collectors.toList());
    }

    @Override
    public List<MenuEntity> allList() {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getInactive, InactiveEnum.FALSE.getCode());
        return baseMapper.selectList(query);
    }

    @Override
    public Map<Integer, List<String>> menuCodeListMap(Map<Integer, List<Integer>> menuListMap) {
        Map<Integer, List<String>> resultMap = new HashMap<>(menuListMap.size());
        if (MapUtil.isNotEmpty(menuListMap)) {
            List<MenuEntity> list = this.getMenuList(menuListMap);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, MenuEntity> map = list.stream()
                        .filter(i-> StrUtil.isNotBlank(i.getApiPerms()))
                        .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
                for (Map.Entry<Integer, List<Integer>> entry : menuListMap.entrySet()) {
                    List<Integer> menuIdList = entry.getValue();
                    List<String> codeList = new ArrayList<>();
                    if (CollUtil.isNotEmpty(menuIdList)) {
                        for (Integer menuId : menuIdList) {
                            MenuEntity menu = map.get(menuId);
                            if (ObjUtil.isNotNull(menu)) {
                                String auths = menu.getApiPerms();
                                if (StrUtil.isNotBlank(auths)) {
                                    codeList.add(auths);
                                }
                            }
                        }
                    }
                    resultMap.put(entry.getKey(), codeList);
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<MenuEntity> list(Collection<Integer> collection) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }
        return this.list(collection, false);
    }

    @Override
    public List<MenuEntity> all() {
        return this.list(Collections.emptyList(), true);
    }

    private List<MenuEntity> list(Collection<Integer> idList, boolean isAll) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getInactive, InactiveEnum.FALSE.getCode());
        query.in(MenuEntity::getMenuType, ListUtil.of(MenuTypeEnum.MENU.getCode(), MenuTypeEnum.POINTS.getCode()));
        if (!isAll) {
            query.in(IdEntity::getId, idList);
        }
        query.orderByAsc(ListUtil.of(MenuEntity::getParentId, MenuEntity::getSort));
        return baseMapper.selectList(query);
    }

    @Override
    public List<MenuEntity> allAndButton() {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getInactive, InactiveEnum.FALSE.getCode());
        query.orderByAsc(ListUtil.of(MenuEntity::getParentId, MenuEntity::getSort));
        return baseMapper.selectList(query);
    }

    @Override
    public List<MenuEntity> listByParentId(Integer parentId) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(MenuEntity::getParentId, parentId);
        return baseMapper.selectList(query);
    }

    @Override
    public boolean delete(Integer id, String reason) {
        return this.removeById(id);
    }

    @Override
    public Map<Integer, List<MenuEntity>> getMenuListMap(Map<Integer, List<Integer>> menuListMap) {
        if (MapUtil.isNotEmpty(menuListMap)) {
            List<MenuEntity> list = this.getMenuList(menuListMap);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, List<MenuEntity>> map = list.stream()
                        .collect(Collectors.groupingBy(MenuEntity::getParentId));
                for (Map.Entry<Integer, List<Integer>> entry : menuListMap.entrySet()) {
                    List<Integer> menuIdList = entry.getValue();
                    List<MenuEntity> menuList = new ArrayList<>();
                    if (CollUtil.isNotEmpty(menuIdList)) {
                        for (Integer menuId : menuIdList) {
                            List<MenuEntity> menu = map.get(menuId);
                            if (CollUtil.isNotEmpty(menu)) {
                                menuList.addAll(menu);
                            }
                        }
                    }
                    map.put(entry.getKey(), menuList);
                }
                return map;
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public List<MenuEntity> listOnlyMenu(Boolean onlyMenu) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        if (BooleanUtil.isTrue(onlyMenu)) {
            query.in(MenuEntity::getMenuType,
                    ListUtil.of(MenuTypeEnum.MENU.getCode(), MenuTypeEnum.CATALOG.getCode()));
        }
        query.eq(BaseEntity::getInactive, false);
        return baseMapper.selectList(query);
    }

    private List<MenuEntity> getMenuList(Map<Integer, List<Integer>> menuListMap) {
        Set<Integer> idList = menuListMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getInactive, InactiveEnum.FALSE.getCode());
        query.in(MenuEntity::getId, idList);
        return baseMapper.selectList(query);
    }

}
