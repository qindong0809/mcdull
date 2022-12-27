package com.dqcer.mcdull.uac.provider.model.dto;

import com.dqcer.framework.base.annotation.EnumsIntValid;
import com.dqcer.framework.base.dto.PagedDTO;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @version 2022-11-16
*/
public class UserLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class, ValidGroup.Delete.class})
     private Long id;
    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsIntValid(groups = {ValidGroup.Status.class}, value = StatusEnum.class)
     private Integer status;

    /**
     * 删除标识（false/正常 true/删除）
     */
    @EnumsIntValid(groups = {ValidGroup.Delete.class}, value = DelFlayEnum.class)
    private Boolean delFlag;


    /**
     * 昵称
     */
    @NotBlank(groups = {ValidGroup.Add.class, ValidGroup.Update.class})
    @Length(groups = {ValidGroup.Add.class, ValidGroup.Update.class}, min = 1, max = 512)
    private String nickname;

    /**
     * 账户
     */
    @NotBlank(groups = {ValidGroup.Add.class, ValidGroup.Update.class})
    @Length(groups = {ValidGroup.Add.class, ValidGroup.Update.class}, min = 5, max = 64)
    private String account;

    /**
     * 电子邮件
     */
    @Length(groups = {ValidGroup.Add.class, ValidGroup.Update.class}, min = 5, max = 64)
    private String email;

    /**
     * 电话
     */
    @Length(groups = {ValidGroup.Add.class, ValidGroup.Update.class}, min = 8, max = 11)
    private String phone;

    /**
     * 角色id集
     */
    private List<Long> roleIds;

    @Override
    public String toString() {
        return "UserLiteDTO{" +
                "id=" + id +
                ", status=" + status +
                ", delFlag=" + delFlag +
                ", nickname='" + nickname + '\'' +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roleIds=" + roleIds +
                "} " + super.toString();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public UserLiteDTO setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }
}