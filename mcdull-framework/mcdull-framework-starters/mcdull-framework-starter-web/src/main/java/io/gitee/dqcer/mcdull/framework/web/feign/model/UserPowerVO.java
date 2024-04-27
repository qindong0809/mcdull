package io.gitee.dqcer.mcdull.framework.web.feign.model;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.List;

/**
 * 用户权力对象
 *
 * @author dqcer
 * @since 2022/12/17
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
    private String code;

    /**
     * 模块
     */
    private List<String> modules;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserPowerVO{");
        sb.append("roleId=").append(roleId);
        sb.append(", code='").append(code).append('\'');
        sb.append(", modules=").append(modules);
        sb.append('}');
        return sb.toString();
    }

    public Long getRoleId() {
        return roleId;
    }

    public UserPowerVO setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UserPowerVO setCode(String code) {
        this.code = code;
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
