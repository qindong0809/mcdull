package com.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.entity.BaseDO;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.util.StrUtil;
import com.dqcer.framework.base.wrapper.CodeEnum;
import com.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import com.dqcer.mcdull.admin.web.dao.mapper.sys.RoleMapper;
import com.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 角色 数据库操作封装实现层
 *
 * @author dqcer
 * @version 2022/12/25
 */
@Service
public class RoleRepositoryImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    @Override
    public Page<RoleDO> selectPage(RoleLiteDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(RoleDO::getName, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(RoleDO entity) {
        entity.setDelFlag(DelFlayEnum.NORMAL.getCode());
        entity.setStatus(StatusEnum.ENABLE.getCode());
        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
        entity.setCreatedTime(new Date());
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }
}