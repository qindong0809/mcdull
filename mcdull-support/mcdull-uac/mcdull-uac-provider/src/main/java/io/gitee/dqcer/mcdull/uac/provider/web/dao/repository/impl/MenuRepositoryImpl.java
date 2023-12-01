package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.MenuMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleMenuRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private IRoleMenuRepository roleMenuRepository;

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
        List<MenuDO> list = baseMapper.selectList(query);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MenuDO::getPerms).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<String>> menuCodeListMap(Collection<Long> roleIdCollection) {
        Map<Long, List<String>> resultMap = new HashMap<>(roleIdCollection.size());
        if (CollUtil.isEmpty(roleIdCollection)) {
            throw new IllegalArgumentException("'roleIDCollection' is empty");
        }
        Map<Long, List<Long>> roleIdValueMenuIdMap = roleMenuRepository.menuIdListMap(roleIdCollection);
        Set<Long> idList = roleIdValueMenuIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(MenuDO::getStatus, StatusEnum.ENABLE.getCode());
        query.in(MenuDO::getPerms, idList);
        List<MenuDO> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            Map<Long, MenuDO> map = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
            for (Map.Entry<Long, List<Long>> entry : roleIdValueMenuIdMap.entrySet()) {
                List<Long> menuIdList = entry.getValue();
                List<String> menuCodeList = menuIdList.stream().map(i -> map.get(i).getPerms()).collect(Collectors.toList());
                resultMap.put(entry.getKey(), menuCodeList);
            }
        }
        return resultMap;
    }
}
