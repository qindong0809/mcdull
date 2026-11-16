package io.gitee.dqcer.mcdull.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.AppUserFocusTimeMapper;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.AppUserMapper;
import io.gitee.dqcer.mcdull.blaze.domain.entity.AppUserEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.AppUserFocusTimeEntity;
import io.gitee.dqcer.mcdull.blaze.service.IFlowFocusService;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * flow-focus 客户端登录与专注数据服务实现
 *
 * @author dqcer
 */
@Service
public class FlowFocusServiceImpl implements IFlowFocusService {

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private AppUserFocusTimeMapper appUserFocusTimeMapper;

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
        appUserMapper.insert(entity);
    }

    @Override
    public void saveFocusData(AppUserFocusTimeEntity entity, Integer userId) {
        entity.setAppUserId(userId);
        entity.setFocusedTime(Convert.toInt(entity.getFocusedTime(), 1));
        appUserFocusTimeMapper.insert(entity);
    }
}
