package com.dqcer.mcdull.auth.api.dto;

import com.dqcer.framework.base.DTO;

public class LoginDTO extends DTO {

    private String username;

    private String pd;

    private String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
