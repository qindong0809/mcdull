package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;


import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.UserConfigMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IUserConfigRepository;
import org.springframework.stereotype.Service;


/**
 * 用户配置存储库 impl
 *
 * @author dqcer
 * @since 2025/03/21
 */
@Service
public class UserConfigRepositoryImpl extends
        CrudRepository<UserConfigMapper, UserConfigEntity> implements IUserConfigRepository {

    @Override
    public void saveOrUpdate(Integer userId, String dateFormat, String timezone, Boolean appendTimezoneStyle) {
        UserConfigEntity userConfig = this.getByUserId(userId);
        if (ObjUtil.isNotNull(userConfig)) {
            userConfig.setDateFormat(dateFormat);
            userConfig.setTimezone(timezone);
            userConfig.setAppendTimezoneStyle(appendTimezoneStyle);
            this.updateById(userConfig);
            return;
        }
        userConfig = new UserConfigEntity();
        userConfig.setUserId(userId);
        userConfig.setDateFormat(dateFormat);
        userConfig.setTimezone(timezone);
        userConfig.setAppendTimezoneStyle(appendTimezoneStyle);
        this.save(userConfig);
    }

    @Override
    public UserConfigEntity getByUserId(Integer userId) {
        LambdaQueryWrapper<UserConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(UserConfigEntity::getUserId, userId);
        return baseMapper.selectOne(query);
    }
}
