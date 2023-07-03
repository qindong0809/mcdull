package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.MenuMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
     * @return {@link Page}<{@link MenuDO}>
     */
    @Override
    public Page<MenuDO> selectPage(MenuLiteDTO dto) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(MenuDO::getName, keyword));
        }
        query.orderByDesc(MiddleDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(MenuDO entity) {
        entity.setCreatedBy(UserContextHolder.currentUserId());
        entity.setCreatedTime(new Date());
        int insert = baseMapper.insert(entity);
        if (insert != 1) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 获取菜单
     *
     * @param menuIds 菜单id
     * @return {@link List}<{@link MenuDO}>
     */
    @Override
    public List<MenuDO> getMenuByIds(List<Long> menuIds) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(MenuDO::getStatus, StatusEnum.ENABLE.getCode());
        query.in(IdDO::getId, menuIds);
        List<MenuDO> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<MenuDO> list(String menuName, String status, List<Long> menuIds) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(menuName)) {
            query.like(MenuDO::getName, menuName);
        }
        if (StrUtil.isNotBlank(status)) {
            query.like(MenuDO::getStatus, status);
        }
        if (CollUtil.isNotEmpty(menuIds)) {
            query.in(IdDO::getId, menuIds);
        }
        List<MenuDO> list = baseMapper.selectList(query);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<MenuDO> getListByName(String name) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        query.eq(MenuDO::getName, name);
        List<MenuDO> list = baseMapper.selectList(query);
        return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
    }

}
