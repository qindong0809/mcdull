package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.util.StringJoiner;

/**
 * 登录 dto
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Schema(name = "用户登录对象")
public class LoginDTO implements DTO {

    @NotBlank(message = "{login.username.not-blank}")
    @Schema(description = "账号", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "{login.password.not-blank}")
    @Schema(description = "密码", example = "sha215(123456)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "唯一标识")
    private String uuid;

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginDTO.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("code='" + code + "'")
                .add("uuid='" + uuid + "'")
                .toString();
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
