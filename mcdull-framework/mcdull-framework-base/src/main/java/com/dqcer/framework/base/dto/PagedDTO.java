package com.dqcer.framework.base.dto;

import com.dqcer.framework.base.validator.ValidGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 分页 DTO
 *
 * @author dqcer
 * @version 2022/07/26
 */
public class PagedDTO extends KeywordDTO implements IPaged {

    /**
     * 每页记录数
     */
    @NotNull(groups = ValidGroup.Paged.class)
    @Max(groups = ValidGroup.Paged.class, value = 1000)
    @Min(groups = ValidGroup.Paged.class, value = 1)
    private Long pageSize;

    /**
     * 当前页数
     */
    @NotNull(groups = ValidGroup.Paged.class)
    @Min(groups = ValidGroup.Paged.class, value = 1)
    private Long currentPage;

    @Override
    public String toString() {
        return "PagedDTO{" +
                "pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                "} " + super.toString();
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 当前页
     *
     * @return {@link Long}
     */
    @Override
    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }
}
