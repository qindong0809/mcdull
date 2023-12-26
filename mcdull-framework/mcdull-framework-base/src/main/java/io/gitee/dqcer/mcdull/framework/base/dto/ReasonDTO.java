package io.gitee.dqcer.mcdull.framework.base.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.StringJoiner;

/**
 * 常用于失活、删除等操作
 *
 * @author dqcer
 * @since 2023/12/26
 */
public class ReasonDTO implements DTO{

    @NotNull
    private Long id;

    @NotBlank
    @Length(min = 512)
    private String reason;

    @Override
    public String toString() {
        return new StringJoiner(", ", ReasonDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("reason='" + reason + "'")
                .toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
