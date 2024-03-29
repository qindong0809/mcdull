package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 常用于失活、删除等操作
 *
 * @author dqcer
 * @since 2023/12/26
 */
public class ReasonDTO implements DTO {

    @NotBlank
    @Length(max = 512)
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
