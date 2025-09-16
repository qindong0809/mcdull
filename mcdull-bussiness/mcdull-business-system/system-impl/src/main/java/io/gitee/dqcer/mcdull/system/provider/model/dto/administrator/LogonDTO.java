package io.gitee.dqcer.mcdull.system.provider.model.dto.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import org.hibernate.validator.constraints.Length;

@Data
public class LogonDTO implements DTO {

    @NotBlank
    @Length(min = 1, max = 32)
    private String loginName;

    @NotBlank
    private String loginPwd;

    @NotBlank
    private String captcha;

    @NotBlank
    private String uuid;

    private String ip;
}
