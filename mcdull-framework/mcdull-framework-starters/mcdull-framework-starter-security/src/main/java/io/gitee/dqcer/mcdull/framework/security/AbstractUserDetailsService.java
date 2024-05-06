package io.gitee.dqcer.mcdull.framework.security;

import cn.dev33.satoken.stp.StpInterface;

import java.util.List;

/**
 * 用户详细信息服务
 *
 * @author dqcer
 * @since 2023/12/01
 */
public abstract class AbstractUserDetailsService implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // TODO: 2023/12/1 缓存
        return permissionList(loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // TODO: 2023/12/1 缓存
        return this.roleList(loginId);
    }

    protected abstract List<String> permissionList(Object loginId);

    protected abstract List<String> roleList(Object loginId);
}
