package com.dqcer.mcdull.uac.api.dto;

import com.dqcer.framework.base.PagedDTO;
import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.annotation.EnumsIntValid;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
     * 删除标识（1/正常 2/删除）
     */
    @EnumsIntValid(groups = {ValidGroup.Delete.class}, value = DelFlayEnum.class)
    private Integer delFlag;


    /**
     * 昵称
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String nickname;

    /**
     * 账户
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 5, max = 64)
    private String account;

    /**
     * 电子邮件
     */
    @Length(groups = {ValidGroup.Add.class}, min = 5, max = 64)
    private String email;

    /**
     * 电话
     */
    @Length(groups = {ValidGroup.Add.class}, min = 8, max = 11)
    private String phone;

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
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}