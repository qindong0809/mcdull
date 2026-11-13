package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppUserEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppUserMapper;
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
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class LoginController extends BasicController {

    @Resource
    private AppUserMapper appUserMapper;
    @Operation(summary = "login")
    @PostMapping("login")
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
    @PostMapping("register")
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

    @Operation(summary = "logout")
    @PostMapping("logout")
    public Result<Boolean> logout() {
        StpKit.APP_USER.logout();
        return Result.success(true);
    }

    // app/guest-mode
    @Operation(summary = "guest-mode")
    @PostMapping("guest-mode")
    public Result<String> guestMode() {
        Map<String, Object> map = Map.of("username", "游客（仅演示专用）", "password", "123456");
        return this.login(map);
    }

    @Operation(summary = "get-user-info")
    @PostMapping("get-user-info")
    public Result<Map<String, Object>> getUserInfo() {
        AppUserEntity user = appUserMapper.selectById(UserContextHolder.userId());
        Map<String, Object> result = Map.of(
            "username", user.getLoginName(),
            "email", user.getEmail()
        );
        return Result.success(result);
    }


    @Operation(summary = "profile")
    @PostMapping("/app/user/profile")
    public Result<Map<String, Object>> profile() {
        AppUserEntity user = appUserMapper.selectById(UserContextHolder.userId());
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getLoginName());
        map.put("email", user.getEmail());
        map.put("gender", user.getGender());
        map.put("stage", user.getStage());
        map.put("theme", user.getTheme());
        map.put("vip", user.getVip());
        map.put("streakDays", 125);
        return Result.success(map);
    }


    @Operation(summary = "saveSettings")
    @PostMapping("/app/user/saveSettings")
    public Result<Boolean> saveSettings(@RequestBody Map<String, Object> map) {
        AppUserEntity user = appUserMapper.selectById(UserContextHolder.userId());
        user.setTheme(Convert.toInt(map.get("theme")));
        appUserMapper.updateById(user);
        return Result.success(true);
    }

    @Operation(summary = "delete")
    @PostMapping("/app/user/delete")
    public Result<Boolean> delete() {
        appUserMapper.deleteById(UserContextHolder.userId());
        return Result.success(true);
    }

    @Operation(summary = "updateProfile")
    @PostMapping("/app/user/updateProfile")
    public Result<Boolean> updateProfile(@RequestBody Map<String, Object> map) {
        AppUserEntity user = appUserMapper.selectById(UserContextHolder.userId());
        user.setGender(Convert.toStr(map.get("gender")));
        user.setStage(Convert.toStr(map.get("stage")));
        appUserMapper.updateById(user);
        return Result.success(true);
    }


    @Operation(summary = "purchaseVip")
    @PostMapping("/app/user/purchaseVip")
    public Result<Map<String, Object>> purchaseVip(@RequestBody Map<String, Object> map) {
        String plan = Convert.toStr(map.get("plan"));
        if (!CharSequenceUtil.equalsAny(plan, "monthly", "yearly")) {
            LogicCheckUtil.throwDataExistException("plan is error");
        }
        Date expireDate = DateUtil.offsetMonth(new Date(), 1);
        if ("yearly".equals(plan)) {
            expireDate = DateUtil.offsetYear(new Date(), 1);
        }

        AppUserEntity user = appUserMapper.selectById(UserContextHolder.userId());
        user.setVip(true);
        appUserMapper.updateById(user);
        return Result.success(Map.of(
            "vip", true,
            "expireDate", DateUtil.format(expireDate, DatePattern.NORM_DATETIME_PATTERN)
        ));
    }

    @Operation(summary = "exportData")
    @PostMapping("/app/user/exportData")
    public void exportData(@RequestBody Map<String, Object> map) {
        String format = Convert.toStr(map.get("format"));
        if (!CharSequenceUtil.equalsAny(format, "json", "csv")) {
            LogicCheckUtil.throwDataExistException("format is error");
        }

        ServletUtil.download("test.json", "good job".getBytes(StandardCharsets.UTF_8));
    }


    @Operation(summary = "我的成就")
    @PostMapping("/app/user/achievements")
    public Result<Map<String, Object>> achievements() {
        return Result.success(Map.of(
            "badges", List.of(
                    Map.of("type", "streak_7", "unlocked", true, "unlockedAt", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN)),
                    Map.of("type", "streak_30", "unlocked", true, "unlockedAt", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN))),
            "totalPoints", 100
        ));
    }


    @Operation(summary = "我的成就")
    @PostMapping("/app/daily/quote")
    public Result<String> dailyQuote() {
        String s = ResourceUtil.readUtf8Str("quote.json");
        List<String> quoteList = JSONUtil.toList(s, String.class);
        // 随机获取一条
        String quote = RandomUtil.randomEle(quoteList);
        return Result.success(quote);
    }
}
