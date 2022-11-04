package com.dqcer.mcdull.auth.api.entity;

import com.dqcer.framework.base.entity.BaseEntity;

import java.time.LocalTime;

public class SysUserEntity extends BaseEntity {

    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer defFlay;

    /**
     * 账户
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 最后登录时间
     */
    private LocalTime lastLoginTime;

    public Integer getDefFlay() {
        return defFlay;
    }

    public void setDefFlay(Integer defFlay) {
        this.defFlay = defFlay;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public LocalTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
