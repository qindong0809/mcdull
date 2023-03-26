package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import javax.validation.constraints.NotNull;

/**
 * 更新状态
 *
 * @author dqcer
 * @since 2022/12/07
 */
public class StatusDTO implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(groups = ValidGroup.Status.class)
    private Long id;

    /**
     * 状态
     * @see StatusEnum
     */
    @EnumsStrValid(value = StatusEnum.class, groups = ValidGroup.Status.class)
    private String status;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusDTO{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
