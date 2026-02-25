package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.UserConfigMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserConfigService;
import org.springframework.stereotype.Service;

@Service
public class UserConfigServiceImpl
    extends BasicCurdServiceImpl<UserConfigMapper, UserConfigEntity> implements IUserConfigService {

    @Override
    public UserConfigEntity get(Integer userId) {
        LambdaQueryWrapper<UserConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(UserConfigEntity::getUserId, userId);
        return baseMapper.selectOne(query);
    }
}
