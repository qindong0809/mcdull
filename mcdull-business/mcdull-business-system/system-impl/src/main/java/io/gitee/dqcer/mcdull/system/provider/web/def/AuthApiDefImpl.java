package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录用户信息内部接口实现。
 * <p>
 * 供业务服务的远程 StpInterface 获取权限与角色；与 system 本地 StpInterfaceImpl
 * 共用缓存 key 和过期策略，避免同一个用户出现两套口径。
 *
 * @author dqcer
 */
@Primary
@RestController
public class AuthApiDefImpl implements AuthApiDef {

    private static final String PERMISSION_CACHE_KEY = "current:user:{}:permission:list";
    private static final String ROLE_CACHE_KEY = "current:user:{}:role:list";
    private static final long CACHE_EXPIRE_SECONDS = 60;

    @Resource
    private ILoginService loginService;
    @Resource
    private IUserService userService;
    @Resource
    private ICache redissonCache;

    @Override
    public InnerResult<RemoteLogonUserVO> getLogonUserInfo(Object loginId, String loginType) {
        Integer userId = Convert.toInt(loginId);
        if (userId == null || userId <= 0) {
            return InnerResult.error(CodeEnum.ERROR_PARAMETERS);
        }
        UserEntity user = userService.get(userId);
        if (user == null) {
            return InnerResult.error(CodeEnum.NOT_FOUND);
        }


        RemoteLogonUserVO vo = new RemoteLogonUserVO();
        vo.setUserId(userId);
        // 当前默认单租户
        vo.setTenantId(1);
        vo.setPermissionList(this.permissionList(userId, user));
        vo.setRoleList(this.roleList(userId));
        return InnerResult.success(vo);
    }

    private List<String> permissionList(Integer userId, UserEntity user) {
        // 内部接口没有 Sa-Token 请求上下文，不能依赖 UserContextHolder.isAdmin()；
        // 直接读库里的管理员标记，与本地 StpInterfaceImpl 的口径保持一致。
        if (Boolean.TRUE.equals(user.getAdministratorFlag())) {
            return ListUtil.of(GlobalConstant.ALL_CODE);
        }
        String key = CharSequenceUtil.format(PERMISSION_CACHE_KEY, userId);
        // 权限为空不缓存：避免刚授予权限后仍被判定无权限
        return redissonCache.getListOrSetPrefixed(key, String.class,
                () -> loginService.getPermissionList(userId), CACHE_EXPIRE_SECONDS, false);
    }

    private List<String> roleList(Integer userId) {
        String key = CharSequenceUtil.format(ROLE_CACHE_KEY, userId);
        // 角色为空不缓存：避免刚分配角色后仍被判定无角色
        return redissonCache.getListOrSetPrefixed(key, String.class,
                () -> loginService.getRoleList(userId), CACHE_EXPIRE_SECONDS, false);
    }
}
