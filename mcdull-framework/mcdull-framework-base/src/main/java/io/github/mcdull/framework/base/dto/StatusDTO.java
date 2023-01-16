package io.gitee.dqcer.framework.base.dto;

import io.gitee.dqcer.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.framework.base.enums.StatusEnum;
import io.gitee.dqcer.framework.base.validator.ValidGroup;

import javax.validation.constraints.NotNull;

/**
 * 更新状态
 *
 * @author dqcer
 * @date 2022/12/07
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
    @EnumsIntValid(value = StatusEnum.class, groups = ValidGroup.Status.class)
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
