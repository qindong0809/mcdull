package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.MenuMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
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
public class MenuRepositoryImpl extends ServiceImpl<MenuMapper, MenuEntity> implements IMenuRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link MenuEntity}>
     */
    @Override
    public Page<MenuEntity> selectPage(MenuLiteDTO dto) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(MenuEntity::getName, keyword));
        }
        query.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(MenuEntity entity) {
        entity.setCreatedBy(UserContextHolder.currentUserId());
        entity.setCreatedTime(new Date());
        entity.setId(this.getMaxId());
        int insert = baseMapper.insert(entity);
        if (insert != 1) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    public synchronized Long getMaxId() {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.orderByDesc(IdEntity::getId);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        MenuEntity menuDO = baseMapper.selectList(query).get(0);
        return menuDO.getId() + 1;
    }

    /**
     * 获取菜单
     *
     * @param menuIds 菜单id
     * @return {@link List}<{@link MenuEntity}>
     */
    @Override
    public List<MenuEntity> getMenuByIds(List<Long> menuIds) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(MenuEntity::getStatus, StatusEnum.ENABLE.getCode());
        query.in(IdEntity::getId, menuIds);
        List<MenuEntity> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<MenuEntity> list(String menuName, String status, List<Long> menuIds) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(menuName)) {
            query.like(MenuEntity::getName, menuName);
        }
        if (StrUtil.isNotBlank(status)) {
            query.like(MenuEntity::getStatus, status);
        }
        if (CollUtil.isNotEmpty(menuIds)) {
            query.in(IdEntity::getId, menuIds);
        }
        List<MenuEntity> list = baseMapper.selectList(query);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<MenuEntity> getListByName(String name) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(MenuEntity::getName, name);
        List<MenuEntity> list = baseMapper.selectList(query);
        return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
    }

    @Override
    public List<MenuEntity> getSubMenuListByParentId(Long parentId) {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(MenuEntity::getParentId, parentId);
        return baseMapper.selectList(query);
    }

    @Override
    public List<MenuEntity> getAllMenu() {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(MenuEntity::getStatus, StatusEnum.ENABLE.getCode());
        return baseMapper.selectList(query);
    }

}
