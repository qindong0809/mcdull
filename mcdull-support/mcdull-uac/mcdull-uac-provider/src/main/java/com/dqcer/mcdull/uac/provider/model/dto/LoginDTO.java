package io.gitee.dqcer.uac.provider.model.dto;

import io.gitee.dqcer.framework.base.dto.DTO;

/**
 * 登录 dto
 *
 * @author dqcer
 * @version 2022/12/26
 */
public class LoginDTO implements DTO {

    private String account;

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
