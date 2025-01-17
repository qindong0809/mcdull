package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录 dto
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class LoginDTO implements DTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "account='" + username + '\'' +
                ", pd='" + password + '\'' +
                ", code='" + code + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public LoginDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
