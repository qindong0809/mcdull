package io.gitee.dqcer.mcdull.framework.security;

import cn.dev33.satoken.stp.StpInterface;
import io.gitee.dqcer.mcdull.framework.spi.permission.UserPermissionInfo;
import io.gitee.dqcer.mcdull.framework.spi.permission.UserPermissionResolver;

import java.util.List;

/**
 * 通用远程权限数据源：把 SPI 权限信息适配成 Sa-Token 的 StpInterface。
 * <p>
 * 由 starter-security 的自动配置注册；业务服务自带本地 StpInterface 时
 * 通过 {@code @ConditionalOnMissingBean} 让位。
 *
 * @author dqcer
 */
public class RemoteUserDetailsService implements StpInterface {

    private final UserPermissionResolver resolver;

    public RemoteUserDetailsService(UserPermissionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return this.info(loginId, loginType).getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return this.info(loginId, loginType).getRoleList();
    }

    private UserPermissionInfo info(Object loginId, String loginType) {
        return resolver.resolve(loginId, loginType);
    }
}
