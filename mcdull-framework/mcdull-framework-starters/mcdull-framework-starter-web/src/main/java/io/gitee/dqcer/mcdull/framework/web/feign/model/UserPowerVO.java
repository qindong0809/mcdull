package io.gitee.dqcer.mcdull.framework.web.feign.model;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

import java.util.List;

/**
 * 用户权力对象
 *
 * @author dqcer
 * @version 2022/12/17
 */
public class UserPowerVO implements VO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 模块
     */
    private List<String> modules;

    @Override
    public String toString() {
        return "UserPowerVO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", modules=" + modules +
                '}';
    }

    public String getRoleName() {
        return roleName;
    }

    public UserPowerVO setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public Long getRoleId() {
        return roleId;
    }

    public UserPowerVO setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public List<String> getModules() {
        return modules;
    }

    public UserPowerVO setModules(List<String> modules) {
        this.modules = modules;
        return this;
    }
}
