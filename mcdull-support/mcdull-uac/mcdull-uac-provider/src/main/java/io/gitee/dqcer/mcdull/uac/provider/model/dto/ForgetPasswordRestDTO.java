package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * forget password
 *
 * @author dqcer
 * @since 2024/06/11
 */
@Data
public class ForgetPasswordRestDTO implements DTO {


    @NotEmpty
    private String token;

    @NotEmpty
    private String newPassword;
}