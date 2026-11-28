package io.gitee.dqcer.mcdull.system.facade.service.adapter;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import io.gitee.dqcer.mcdull.framework.spi.permission.UserPermissionInfo;
import io.gitee.dqcer.mcdull.framework.spi.permission.UserPermissionResolver;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户权限 SPI 适配器：缓存 + system-feign 回源。
 * <p>
 * 一次远程调用同时取回权限与角色，避免 Sa-Token 在同一请求内分别回调
 * getPermissionList / getRoleList 时打两次 Feign。system 不可用或接口异常时
 * 降级为空快照（fail-closed），且不长期污染缓存。
 *
 * @author dqcer
 */
@Component
public class UserPermissionFeignResolver implements UserPermissionResolver {

    private static final Logger log = LoggerFactory.getLogger(UserPermissionFeignResolver.class);

    private static final String CACHE_KEY = "current:user:{}:permission:snapshot";
    private static final long CACHE_EXPIRE = 60L;

    @Resource
    private AuthApiDef authApi;

    @Resource
    private ICache cache;

    @Override
    public UserPermissionInfo resolve(Object loginId, String loginType) {
        Integer userId = parseUserId(loginId);
        if (userId == null) {
            return UserPermissionInfo.EMPTY;
        }
        String key = CACHE_KEY.replace("{}", String.valueOf(userId));
        RemoteLogonUserVO vo = cache.getOrSetPrefixed(key, RemoteLogonUserVO.class, () -> {
            try {
                RemoteLogonUserVO remote = authApi.getLogonUserInfo(userId, loginType).unwrap();
                // 保证 getter 返回非 null（空权限/空角色统一用空列表表达）
                remote.setPermissionList(remote.getPermissionList() == null
                        ? List.of() : List.copyOf(remote.getPermissionList()));
                remote.setRoleList(remote.getRoleList() == null
                        ? List.of() : List.copyOf(remote.getRoleList()));
                if (remote.getPermissionList().isEmpty() && remote.getRoleList().isEmpty()) {
                    // 空结果走框架的短周期哨兵（30s），避免刚授权后长时间无权限
                    return null;
                }
                return remote;
            } catch (Exception ex) {
                LogHelp.warn(log, "远程解析用户权限失败，降级为空. loginId={}, cause={}",
                        loginId, ex.getMessage());
                return null;
            }
        }, CACHE_EXPIRE);
        return vo == null ? UserPermissionInfo.EMPTY : vo;
    }

    private Integer parseUserId(Object loginId) {
        if (loginId == null) {
            return null;
        }
        try {
            Integer userId = Integer.valueOf(String.valueOf(loginId));
            return userId > 0 ? userId : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
