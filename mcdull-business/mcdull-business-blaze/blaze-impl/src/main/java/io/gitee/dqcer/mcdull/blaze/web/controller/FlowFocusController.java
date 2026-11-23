package io.gitee.dqcer.mcdull.blaze.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.blaze.model.entity.AppUserEntity;
import io.gitee.dqcer.mcdull.blaze.model.entity.AppUserFocusTimeEntity;
import io.gitee.dqcer.mcdull.blaze.web.service.IFlowFocusService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUserSession;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.mysql.util.LogicCheckUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;


@RestController
public class FlowFocusController extends BasicController {

    @Resource
    private IFlowFocusService flowFocusService;

    @Operation(summary = "login")
    @PostMapping(GlobalConstant.IGNORE_URL + "/flow-focus/auth/login")
    public Result<String> login(@RequestBody Map<String, Object> map) {
        String username = Convert.toStr(map.get("username"));
        String password = Convert.toStr(map.get("password"));
        if (!CharSequenceUtil.isAllNotBlank(username, password)) {
            LogicCheckUtil.throwMissingParmeterException("username or password is empty", username, password);
        }
        AppUserEntity user = flowFocusService.authenticate(username, password);
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
        String email = Convert.toStr(map.get("email"));
        if (!CharSequenceUtil.isAllNotBlank(username, password, email)) {
            LogicCheckUtil.throwMissingParmeterException("username or password or email is empty", username, password, email);
        }
        return super.locker(username, () -> {
            flowFocusService.register(username, password, email);
            return Result.success(true);
        });
    }

    //save-focus-data
    @PostMapping(GlobalConstant.APP_PATH +"/flow-focus/save-focus-data")
    public Result<Boolean> saveFocusData(@RequestBody Map<String, Object> map) {
        AppUserFocusTimeEntity bean = BeanUtil.toBean(map, AppUserFocusTimeEntity.class);
        flowFocusService.saveFocusData(bean, UserContextHolder.userId());
        return Result.success(true);
    }


}
