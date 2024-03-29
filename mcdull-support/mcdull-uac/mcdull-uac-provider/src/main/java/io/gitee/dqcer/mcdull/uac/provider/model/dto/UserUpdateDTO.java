package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
public class UserUpdateDTO implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    @NotBlank
    @Length(min = 1, max = 512)
    private String nickname;

    /**
     * 账户
     */
    @NotBlank
    @Length(min = 5, max = 64)
    private String account;

    /**
     * 电子邮件
     */
    @Email
    private String email;

    /**
     * 电话
     */
    @Length(min = 8, max = 11)
    private String phone;

    /**
     * 角色id集
     */
    @NotEmpty
    @Size(min = 1)
    private List<Integer> roleIds;

    @Override
    public String toString() {
        return "UserLiteDTO{" +
                ", nickname='" + nickname + '\'' +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roleIds=" + roleIds +
                "} " + super.toString();
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
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