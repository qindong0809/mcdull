package com.dqcer.mcdull.admin.model.dto.sys;

import com.dqcer.framework.base.dto.DTO;

import javax.validation.constraints.NotEmpty;

/**
 * 登录 dto
 *
 * @author dqcer
 * @version 2022/12/26
 */
public class LoginDTO implements DTO {

    @NotEmpty
    private String account;

    @NotEmpty
    private String pd;

    private String code;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "account='" + account + '\'' +
                ", pd='" + pd + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
