package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * password policy vo
 * @author dqcer
 * @since 2024/06/03
 */
@Data
public class PasswordPolicyDTO implements DTO {

    @NotNull
    @Schema(description = "与旧密码重复次数")
    private Integer repeatablePasswordNumber;

    @NotNull
    @Schema(description = "登录失败最大次数")
    private Integer failedLoginMaximumNumber;

    @NotNull
    @Schema(description = "登录失败的时间")
    private Integer failedLoginMaximumTime;

    @NotNull
    @Schema(description = "密码过期时间")
    private Integer passwordExpiredPeriod;
}
