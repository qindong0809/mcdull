package com.dqcer.framework.base.dto;

import com.dqcer.framework.base.annotation.EnumsIntValid;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.validator.ValidGroup;

import javax.validation.constraints.NotNull;

/**
 * 更新状态
 *
 * @author dqcer
 * @date 2022/12/07
 */
public class StatusDTO implements DTO {

    private static final long serialVersionUID = 6698271402380430318L;

    /**
     * id
     */
    @NotNull(groups = ValidGroup.Status.class)
    private Long id;

    /**
     * 状态
     * @see com.dqcer.framework.base.enums.StatusEnum
     */
    @EnumsIntValid(value = StatusEnum.class, groups = ValidGroup.Status.class)
    private Integer status;

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