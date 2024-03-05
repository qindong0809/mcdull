package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
public class UserLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class, ValidGroup.Delete.class})
     private Integer id;
    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsIntValid(groups = {ValidGroup.Status.class}, value = StatusEnum.class)
     private String status;


    /**
     * 昵称
     */
    @NotBlank(groups = {ValidGroup.Insert.class, ValidGroup.Update.class})
    @Length(groups = {ValidGroup.Insert.class, ValidGroup.Update.class}, min = 1, max = 512)
    private String nickname;

    /**
     * 账户
     */
    @NotBlank(groups = {ValidGroup.Insert.class, ValidGroup.Update.class})
    @Length(groups = {ValidGroup.Insert.class, ValidGroup.Update.class}, min = 5, max = 64)
    private String account;

    /**
     * 电子邮件
     */
    @Length(groups = {ValidGroup.Insert.class, ValidGroup.Update.class}, min = 5, max = 64)
    private String email;

    /**
     * 电话
     */
    @Length(groups = {ValidGroup.Insert.class, ValidGroup.Update.class}, min = 8, max = 11)
    private String phone;

    /**
     * 角色id集
     */
    private List<Integer> roleIds;

    @Override
    public String toString() {
        return "UserLiteDTO{" +
                "id=" + id +
                ", status=" + status +
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}