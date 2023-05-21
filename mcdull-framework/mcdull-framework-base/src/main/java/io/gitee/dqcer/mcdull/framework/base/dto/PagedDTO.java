package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.supert.IPaged;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.StringJoiner;


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

    /**
     * 不需要分页
     */
    protected Boolean notNeedPaged;

    @Override
    public String toString() {
        return new StringJoiner(", ", PagedDTO.class.getSimpleName() + "[", "]")
                .add("pageSize=" + pageSize)
                .add("pageNum=" + pageNum)
                .add("orders=" + orders)
                .add("notNeedPaged=" + notNeedPaged)
                .add("keyword='" + keyword + "'")
                .toString();
    }

    @Override
    public Long getPageSize() {
        if (this.notNeedPaged != null) {
            if (notNeedPaged) {
                return GlobalConstant.EXCEL_EXPORT_MAX_ROW;
            }
        }
        return pageSize;
    }

    public PagedDTO setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public Long getPageNum() {
        return pageNum;
    }

    public PagedDTO setPageNum(Long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public PagedDTO setOrders(List<OrderItem> orders) {
        this.orders = orders;
        return this;
    }

    public Boolean getNotNeedPaged() {
        return notNeedPaged;
    }

    public void setNotNeedPaged(Boolean notNeedPaged) {
        this.notNeedPaged = notNeedPaged;
    }
}
