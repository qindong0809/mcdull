package com.dqcer.mcdull.uac.api.dto;

import com.dqcer.framework.base.dto.DTO;

public class LoginDTO implements DTO {

    private String account;

    private String pd;

    private String code;

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
