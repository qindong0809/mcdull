package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;
import java.util.StringJoiner;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Schema
public class UserInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "昵称", minLength = 1, maxLength = 512)
    @NotBlank
    @Length(min = 1, max = 512)
    private String nickname;

    @Schema(description = "昵称", minLength = 1, maxLength = 512)
    @NotBlank
    @Length(min = 5, max = 64)
    private String account;

    @NotBlank
    @Schema(description = "电子邮件", minLength = 1, maxLength = 512)
    @Email
    private String email;

    @NotBlank
    @Schema(description = "电话", minLength = 8, maxLength = 11)
    @Length(min = 8, max = 11)
    private String phone;

    @NotEmpty
    @Schema(description = "角色id集", minProperties = 1)
    @Size(min = 1)
    private List<Long> roleIds;

    @NotNull
    @Schema(description = "部门id")
    private Long deptId;

    @Override
    public String toString() {
        return new StringJoiner(", ", UserInsertDTO.class.getSimpleName() + "[", "]")
                .add("nickname='" + nickname + "'")
                .add("account='" + account + "'")
                .add("email='" + email + "'")
                .add("phone='" + phone + "'")
                .add("roleIds=" + roleIds)
                .add("deptId=" + deptId)
                .toString();
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}