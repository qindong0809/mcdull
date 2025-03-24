package io.gitee.dqcer.mcdull.uac.provider.flow.login;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginDeviceEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserConfigRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component(value = "Authority")
@TreeNode(code = "Authority")
public class Authority implements ProcessHandler<LoginContext> {

    @Resource
    private ILoginService loginService;

    @Resource
    private IUserConfigRepository userConfigRepository;

    @Override
    public void execute(LoginContext context) {
        LoginDTO dto = context.getLoginDTO();
        Dict dict = context.getDict();
        UserEntity userEntity = dict.get("user", new UserEntity());
        LoginDeviceEnum  deviceEnum = IEnum.getByCode(LoginDeviceEnum.class, dto.getLoginDevice());
        String device = LoginDeviceEnum.PC.getText();
        if (ObjUtil.isNotNull(deviceEnum)) {
            device = deviceEnum.getText();
        }
        StpUtil.login(userEntity.getId(), device);
        CacheUser cache = this.buildCacheUser(userEntity);
        updateUserSession(cache);
        StpUtil.getSession().set(GlobalConstant.CACHE_CURRENT_USER, cache);
        context.setVo(loginService.buildLogonVo(userEntity));
        dict.put("typeEnum", LoginLogResultTypeEnum.LOGIN_SUCCESS);
        context.setDict(dict);
    }

    private static void updateUserSession(CacheUser cacheUser) {
        UnifySession session = UserContextHolder.getSession();
        session.copyCommon(cacheUser, session);
        UserContextHolder.setSession(session);
    }

    private CacheUser buildCacheUser(UserEntity entity) {
        CacheUser cache = new CacheUser();
        cache.setUserId(String.valueOf(entity.getId()));
        cache.setTenantId(entity.getDepartmentId());
        cache.setAdministratorFlag(entity.getAdministratorFlag());
        cache.setLanguage(Locale.SIMPLIFIED_CHINESE.getLanguage());
        cache.setLoginName(entity.getLoginName());
        UserConfigEntity userConfig = userConfigRepository.getByUserId(entity.getId());
        if (ObjUtil.isNotNull(userConfig)) {
            cache.setDateFormat(userConfig.getDateFormat());
            cache.setZoneIdStr(userConfig.getTimezone());
            cache.setAppendTimezoneStyle(userConfig.getAppendTimezoneStyle());
        } else {
            cache.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            cache.setZoneIdStr("Asia/Shanghai");
            cache.setAppendTimezoneStyle(false);
        }
        return cache;
    }

    @Override
    public boolean extendParentNodeTryAttr() {
        return true;
    }

    @Override
    public boolean endNode() {
        return true;
    }
}
