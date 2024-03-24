package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.MenuMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Service
public class MenuRepositoryImpl extends ServiceImpl<MenuMapper, MenuDO> implements IMenuRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    @Override
    public Page<MenuDO> selectPage(MenuLiteDTO dto) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(MenuDO::getName, keyword));
        }
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public List<String> allCodeList() {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
        List<MenuDO> list = baseMapper.selectList(query);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MenuDO::getAuths).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, List<String>> menuCodeListMap(Map<Integer, List<Integer>> menuListMap) {
        Map<Integer, List<String>> resultMap = new HashMap<>(menuListMap.size());
        if (MapUtil.isNotEmpty(menuListMap)) {
            Set<Integer> idList = menuListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
            query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
            query.in(MenuDO::getId, idList);
            List<MenuDO> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, MenuDO> map = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
                for (Map.Entry<Integer, List<Integer>> entry : menuListMap.entrySet()) {
                    List<Integer> menuIdList = entry.getValue();
                    List<String> menuCodeList = menuIdList.stream()
                            .map(i -> map.get(i).getAuths()).collect(Collectors.toList());
                    resultMap.put(entry.getKey(), menuCodeList);
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<MenuDO> list(Collection<Integer> collection) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }
        return this.list(collection, false);
    }

    @Override
    public List<MenuDO> all() {
        return this.list(Collections.emptyList(), true);
    }

    private List<MenuDO> list(Collection<Integer> idList, boolean isAll) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
        query.in(MenuDO::getMenuType, ListUtil.of(MenuTypeEnum.MENU.getCode(), MenuTypeEnum.IFRAME.getCode(), MenuTypeEnum.LINK.getCode()));
        if (!isAll) {
            query.in(IdDO::getId, idList);
        }
        query.orderByAsc(ListUtil.of(MenuDO::getParentId, MenuDO::getRankOrder));
        return baseMapper.selectList(query);
    }

    @Override
    public List<MenuDO> allAndButton() {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
        query.orderByAsc(ListUtil.of(MenuDO::getParentId, MenuDO::getRankOrder));
        return baseMapper.selectList(query);
    }

    @Override
    public List<MenuDO> listByParentId(Integer parentId) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(MenuDO::getParentId, parentId);
        return baseMapper.selectList(query);
    }

    @Override
    public boolean delete(Integer id, String reason) {
        return this.removeById(id);
    }

}
