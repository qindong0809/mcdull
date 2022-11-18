package com.dqcer.framework.base;

import com.dqcer.framework.base.page.IPaged;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 分页 DTO
 *
 * @author dqcer
 * @version 2022/07/26
 */
@SuppressWarnings("unused")
public abstract class PagedDTO extends KeywordDTO implements IPaged {

    /**
     * 每页记录数
     */
    @NotNull(groups = ValidGroup.Paged.class)
    @Max(groups = ValidGroup.Paged.class, value = 1000)
    private Long pageSize;

    /**
     * 当前页数
     */
    @NotNull(groups = ValidGroup.Paged.class)
    @Min(groups = ValidGroup.Paged.class, value = 1)
    private Long currPage;

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Long getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Long currPage) {
        this.currPage = currPage;
    }
}
