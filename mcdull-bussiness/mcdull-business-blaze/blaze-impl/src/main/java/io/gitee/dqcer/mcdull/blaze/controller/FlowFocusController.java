package io.gitee.dqcer.mcdull.blaze.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.AppUserFocusTimeMapper;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.AppUserMapper;
import io.gitee.dqcer.mcdull.blaze.domain.entity.AppUserEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.AppUserFocusTimeEntity;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUserSession;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;


@RestController
public class FlowFocusController extends BasicController {

    @Resource
    private AppUserMapper appUserMapper;
    @Resource
    private AppUserFocusTimeMapper appUserFocusTimeMapper;

    @Operation(summary = "login")
    @PostMapping(GlobalConstant.IGNORE_URL + "/flow-focus/auth/login")
    public Result<String> login(@RequestBody Map<String, Object> map) {
        String username = Convert.toStr(map.get("username"));
        String password = Convert.toStr(map.get("password"));
        if (!CharSequenceUtil.isAllNotBlank(username, password)) {
            LogicCheckUtil.throwMissingParmeterException("username or password is empty", username, password);
        }
        String md5Password = Md5Util.getMd5(password);
        LambdaQueryWrapper<AppUserEntity> query = Wrappers.lambdaQuery();
        query.eq(AppUserEntity::getLoginName, username)
                .eq(AppUserEntity::getLoginPwd, md5Password);
        List<AppUserEntity> userList = appUserMapper.selectList(query);
        if (CollUtil.isEmpty(userList)) {
            throw new BusinessException("system.authentication.failure");
        }
        AppUserEntity user = userList.get(0);
        StpKit.APP_USER.login(user.getId());

        CacheUserSession cache = this.getCacheUserSession(user);
        UnifySession session = UserContextHolder.getSession();
        session.copyCommon(cache);
        UserContextHolder.setSession(session);
        StpKit.APP_USER.getSessionByLoginId(user.getId(), true)
            .set(GlobalConstant.CACHE_CURRENT_APP_USER, cache);

        return Result.success(StpKit.APP_USER.getTokenValue());
    }

    private CacheUserSession getCacheUserSession(AppUserEntity user) {
        CacheUserSession cache = new CacheUserSession();
        cache.setUserId(user.getId().toString());
        cache.setTenantId(GlobalConstant.Number.NUMBER_0);
        cache.setLanguage(Locale.SIMPLIFIED_CHINESE.getLanguage());
        cache.setLoginName(user.getLoginName());
        cache.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        cache.setZoneIdStr("Asia/Shanghai");
        cache.setAppendTimezoneStyle(false);
        return cache;
    }

    @Operation(summary = "register")
    @PostMapping(GlobalConstant.IGNORE_URL + "/flow-focus/auth/register")
    public Result<Boolean> register(@RequestBody Map<String, Object> map) {
        String username = Convert.toStr(map.get("username"));
        String password = Convert.toStr(map.get("password"));
        String email =  Convert.toStr(map.get("email"));
        if (!CharSequenceUtil.isAllNotBlank(username, password, email)) {
            LogicCheckUtil.throwMissingParmeterException("username or password or email is empty", username, password, email);
        }
        return super.locker(username, () -> {
            String md5Password = Md5Util.getMd5(password);
            LambdaQueryWrapper<AppUserEntity> query = Wrappers.lambdaQuery();
            query.eq(AppUserEntity::getLoginName, username);
            boolean exists = appUserMapper.exists(query);
            if (exists) {
                LogicCheckUtil.throwDataExistException(username);
            }
            AppUserEntity entity = new AppUserEntity();
            entity.setLoginName(username);
            entity.setLoginPwd(md5Password);
            entity.setEmail(email);
            appUserMapper.insert(entity);
            return Result.success(true);
        });
    }

    //save-focus-data
    @PostMapping(GlobalConstant.APP_PATH +"/flow-focus/save-focus-data")
    public Result<Boolean> saveFocusData(@RequestBody Map<String, Object> map) {
        AppUserFocusTimeEntity bean = BeanUtil.toBean(map, AppUserFocusTimeEntity.class);
        bean.setAppUserId(UserContextHolder.userId());
        bean.setFocusedTime(Convert.toInt(bean.getFocusedTime(), 1));
        appUserFocusTimeMapper.insert(bean);
        return Result.success(true);
    }


}
