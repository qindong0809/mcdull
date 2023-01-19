package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import javax.validation.constraints.NotNull;

/**
 * 查询通过id
 *
 * @author dqcer
 * @version 2023/01/19
 */
public class QueryByIdDTO implements DTO{

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

    public QueryByIdDTO setId(Long id) {
        this.id = id;
        return this;
    }
}
