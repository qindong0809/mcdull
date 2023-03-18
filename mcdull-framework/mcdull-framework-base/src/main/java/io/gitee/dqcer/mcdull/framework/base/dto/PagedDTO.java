package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 分页 DTO
 *
 * @author dqcer
 * @since 2022/07/26
 */
public class PagedDTO extends KeywordDTO implements IPaged {

    /**
     * 每页记录数
     */
    @NotNull(groups = ValidGroup.Paged.class)
    @Max(groups = ValidGroup.Paged.class, value = 1000)
    @Min(groups = ValidGroup.Paged.class, value = 1)
    protected Long pageSize;

    /**
     * 当前页数
     */
    @NotNull(groups = ValidGroup.Paged.class)
    @Min(groups = ValidGroup.Paged.class, value = 1)
    protected Long pageNum;

    /**
     * 排序字段信息
     */
    protected List<OrderItem> orders;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PagedDTO{");
        sb.append("pageSize=").append(pageSize);
        sb.append(", page=").append(pageNum);
        sb.append(", orders=").append(orders);
        sb.append(", keyword='").append(keyword).append('\'');
        sb.append('}');
        return sb.toString();
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
    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }
}
