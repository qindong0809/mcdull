package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginDeviceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 登录 dto
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Getter
@Setter
@ToString
@Schema(name = "用户登录对象")
public class LoginDTO implements DTO {

    @Schema(description = "登录账号", example = "admin")
    @NotBlank(message = "{login.username.not-blank}")
    @Length(max = 30, message = "登录账号最多30字符")
    private String loginName;

    @Schema(description = "密码", example = "prtRvwLK8GXkHuWSGL50LQ==")
    @NotBlank(message = "{login.password.not-blank}")
    private String password;

    @Schema(description = "验证码")
    private String captchaCode;

    @Schema(description = "验证码uuid标识")
    private String captchaUuid;

    @EnumsIntValid(value = LoginDeviceEnum.class)
    @SchemaEnum(desc = "登录终端", value = LoginDeviceEnum.class)
    private Integer loginDevice;

}
