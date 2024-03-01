package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.StringJoiner;

/**
 * 登录VO
 *
 * @author dqcer
 * @since 2022/11/01
 */
@Schema(name = "用户登录对象")
public class LoginUserVO implements VO {

    private UserVO user;

    private List<String> roleCodeList;

    private List<String> permissionCodeList;

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginUserVO.class.getSimpleName() + "[", "]")
                .add("user=" + user)
                .add("roleCodeList=" + roleCodeList)
                .add("permissionCodeList=" + permissionCodeList)
                .toString();
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public List<String> getRoleCodeList() {
        return roleCodeList;
    }

    public void setRoleCodeList(List<String> roleCodeList) {
        this.roleCodeList = roleCodeList;
    }

    public List<String> getPermissionCodeList() {
        return permissionCodeList;
    }

    public void setPermissionCodeList(List<String> permissionCodeList) {
        this.permissionCodeList = permissionCodeList;
    }
}
