package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

/**
 * 登录 dto
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Schema(name = "Login DTO")
public class LoginDTO implements DTO {

    @NotBlank
    @Schema(description = "account info", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String account;

    @NotBlank
    @Schema(description = "password info", example = "sha215(123456)", requiredMode = Schema.RequiredMode.REQUIRED)
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
