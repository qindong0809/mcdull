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
        // 返回此 loginId 拥有的权限列表
        // TODO: 2023/12/1 缓存
        return permissionList(loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        // TODO: 2023/12/1 缓存
        return this.roleList(loginId);
    }

    /**
     * 权限列表
     *
     * @param loginId 登录id
     * @return {@link List}<{@link String}>
     */
    protected abstract List<String> permissionList(Object loginId);

    /**
     * 角色列表
     *
     * @param loginId 登录id
     * @return {@link List}<{@link String}>
     */
    protected abstract List<String> roleList(Object loginId);
}
