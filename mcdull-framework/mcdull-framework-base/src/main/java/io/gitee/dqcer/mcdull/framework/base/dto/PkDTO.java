package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import javax.validation.constraints.NotNull;

/**
 * @author dqcer
 * @since 2023/01/19
 */
public class PkDTO implements DTO{

    @NotNull(groups = ValidGroup.Id.class)
    private Long id;

    @Override
    public String toString() {
        return "QueryByIdDTO{" +
                "id=" + id +
                '}';
    }

    public Long getId() {
        return id;
    }

    public PkDTO setId(Long id) {
        this.id = id;
        return this;
    }
}
