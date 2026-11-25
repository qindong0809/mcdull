package io.gitee.dqcer.mcdull.focus.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppUserEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppUserMapper;
import io.gitee.dqcer.mcdull.focus.web.service.IAppUserService;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.mysql.util.LogicCheckUtil;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用用户服务实现
 *
 * @author dqcer
 */
@Service
public class AppUserServiceImpl implements IAppUserService {

    @Resource
    private AppUserMapper appUserMapper;

    @Override
    public AppUserEntity authenticate(String loginName, String password) {
        LambdaQueryWrapper<AppUserEntity> query = Wrappers.lambdaQuery();
        query.eq(AppUserEntity::getLoginName, loginName)
            .eq(AppUserEntity::getLoginPwd, Md5Util.getMd5(password));
        List<AppUserEntity> userList = appUserMapper.selectList(query);
        if (CollUtil.isEmpty(userList)) {
            throw new BusinessException("system.authentication.failure");
        }
        return userList.get(0);
    }

    @Override
    public void register(String loginName, String password, String email) {
        LambdaQueryWrapper<AppUserEntity> query = Wrappers.lambdaQuery();
        query.eq(AppUserEntity::getLoginName, loginName);
        if (appUserMapper.exists(query)) {
            LogicCheckUtil.throwDataExistException(loginName);
        }
        AppUserEntity entity = new AppUserEntity();
        entity.setLoginName(loginName);
        entity.setLoginPwd(Md5Util.getMd5(password));
        entity.setEmail(email);
        try {
            appUserMapper.insert(entity);
        } catch (DuplicateKeyException e) {
            LogicCheckUtil.throwDataExistException(loginName);
        }
    }

    @Override
    public AppUserEntity getByUserId(Integer userId) {
        return appUserMapper.selectById(userId);
    }

    @Override
    public void updateTheme(Integer userId, Integer theme) {
        AppUserEntity user = getByUserId(userId);
        user.setTheme(theme);
        appUserMapper.updateById(user);
    }

    @Override
    public void updateProfile(Integer userId, String gender, String stage) {
        AppUserEntity user = getByUserId(userId);
        user.setGender(gender);
        user.setStage(stage);
        appUserMapper.updateById(user);
    }

    @Override
    public void openVip(Integer userId) {
        AppUserEntity user = getByUserId(userId);
        user.setVip(true);
        appUserMapper.updateById(user);
    }

    @Override
    public void deleteById(Integer userId) {
        appUserMapper.deleteById(userId);
    }
}
