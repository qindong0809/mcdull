package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * password policy vo
 *
 * @author dqcer
 * @since 2024/06/03
 */
@Data
public class PasswordPolicyVO implements VO {

    @Schema(description = "与旧密码重复次数")
    private Integer repeatablePasswordNumber;

    @Schema(description = "登录失败最大次数")
    private Integer failedLoginMaximumNumber;

    @Schema(description = "登录失败的时间")
    private Integer failedLoginMaximumTime;

    @Schema(description = "密码过期时间")
    private Integer passwordExpiredPeriod;
}
