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
import io.gitee.dqcer.mcdull.uac.provider.model.enums.StatusEnum;
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
        query.eq(MenuDO::getStatus, StatusEnum.ENABLE.getCode());
        query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
        List<MenuDO> list = baseMapper.selectList(query);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MenuDO::getPerms).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<String>> menuCodeListMap(Map<Long, List<Long>> menuListMap) {
        Map<Long, List<String>> resultMap = new HashMap<>(menuListMap.size());
        if (MapUtil.isNotEmpty(menuListMap)) {
            Set<Long> idList = menuListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
            query.eq(MenuDO::getStatus, StatusEnum.ENABLE.getCode());
            query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
            query.in(MenuDO::getPerms, idList);
            List<MenuDO> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Long, MenuDO> map = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
                for (Map.Entry<Long, List<Long>> entry : menuListMap.entrySet()) {
                    List<Long> menuIdList = entry.getValue();
                    List<String> menuCodeList = menuIdList.stream()
                            .map(i -> map.get(i).getPerms()).collect(Collectors.toList());
                    resultMap.put(entry.getKey(), menuCodeList);
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<MenuDO> list(Collection<Long> collection) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getInactive, InactiveEnum.FALSE.getCode());
        query.eq(MenuDO::getStatus, true);
        query.in(MenuDO::getMenuType, ListUtil.of(MenuTypeEnum.DIRECTORY.getCode(), MenuTypeEnum.MENU.getCode()));
        query.in(IdDO::getId, collection);
        query.orderByAsc(ListUtil.of(MenuDO::getParentId, MenuDO::getOrderNum));
        return baseMapper.selectList(query);
    }
}
