package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

import java.util.Set;

/**
 * 登录VO
 *
 * @author dqcer
 * @since 2022/11/01
 */
public class CurrentUserInfVO implements VO {

    private UserVO user;

    private Set<String> roles;

    private Set<String> permissions;

    @Override
    public String toString() {
        return "CurrentUserInfVO{" +
                "user=" + user +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }

    public UserVO getUser() {
        return user;
    }

    public CurrentUserInfVO setUser(UserVO user) {
        this.user = user;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public CurrentUserInfVO setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public CurrentUserInfVO setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }
}
