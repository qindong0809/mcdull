package com.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.utils.StrUtil;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.SysUserEntity;
import com.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.springframework.stereotype.Service;

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
        return baseMapper.selectPage(new Page<>(dto.getCurrPage(), dto.getPageSize()), query);
    }
}
