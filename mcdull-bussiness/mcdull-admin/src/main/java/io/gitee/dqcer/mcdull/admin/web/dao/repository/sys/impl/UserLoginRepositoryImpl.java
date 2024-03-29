package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginInfoLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserLoginEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.UserLoginMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserLoginRepository;
import org.springframework.stereotype.Service;

/**
 * 用户登录信息 数据库操作实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserLoginRepositoryImpl extends ServiceImpl<UserLoginMapper, UserLoginEntity> implements IUserLoginRepository {


    @Override
    public Page<UserLoginEntity> paged(LoginInfoLiteDTO dto) {
        LambdaQueryWrapper<UserLoginEntity> lambda = new QueryWrapper<UserLoginEntity>().lambda();
        lambda.orderByDesc(UserLoginEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lambda);
    }
}
