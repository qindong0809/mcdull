package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;

import javax.validation.constraints.NotNull;

/**
 * @author dqcer
 * @since 2023/01/19
 */
public class PkDTO implements DTO {

    @NotNull
    private Integer id;

    @Override
    public String toString() {
        return "QueryByIdDTO{" +
                "id=" + id +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public PkDTO setId(Integer id) {
        this.id = id;
        return this;
    }
}
