package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

/**
 * forget password
 *
 * @author dqcer
 * @since 2024/06/11
 */
@Data
public class ForgetPasswordRequestDTO implements DTO {

    /**
     * login name
     */
    @NotNull
    @Length(max = 300)
    private String userIdentity;
}
