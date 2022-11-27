package com.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.auth.UserContextHolder;
import com.dqcer.framework.base.entity.BaseEntity;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.utils.StrUtil;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.SysUserEntity;
import com.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, SysUserEntity> implements IUserRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link SysUserEntity}>
     */
    @Override
    public Page<SysUserEntity> selectPage(UserLiteDTO dto) {
        LambdaQueryWrapper<SysUserEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(SysUserEntity::getAccount, keyword).or().like(SysUserEntity::getPhone, keyword).or().like(SysUserEntity::getEmail, keyword));
        }
        query.orderByDesc(BaseEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrPage(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(SysUserEntity entity) {
        entity.setDelFlag(DelFlayEnum.NORMAL.getCode());
        entity.setStatus(StatusEnum.ENABLE.getCode());
        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
        entity.setCreatedTime(new Date());
        int insert = baseMapper.insert(entity);
        if (insert != 1) {
            throw new BusinessException(ResultCode.DB_ERROR);
        }
        return entity.getId();
    }
}
