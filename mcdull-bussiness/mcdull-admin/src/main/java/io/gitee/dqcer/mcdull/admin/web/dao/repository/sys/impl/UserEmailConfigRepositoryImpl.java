package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserEmailConfigEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.UserEmailConfigMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserEmailConfigRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.springframework.stereotype.Service;

/**
* @author dqcer
* @since 2023-01-14
*/
@Service
public class UserEmailConfigRepositoryImpl extends ServiceImpl<UserEmailConfigMapper, UserEmailConfigEntity>  implements IUserEmailConfigRepository {

    @Override
    public UserEmailConfigEntity getOneByUserId(Long userId) {
        LambdaQueryWrapper<UserEmailConfigEntity> lambda = Wrappers.lambdaQuery();
        lambda.eq(UserEmailConfigEntity::getUserId, userId);
        lambda.last(GlobalConstant.Database.SQL_LIMIT_1);
        return baseMapper.selectOne(lambda);
    }
}