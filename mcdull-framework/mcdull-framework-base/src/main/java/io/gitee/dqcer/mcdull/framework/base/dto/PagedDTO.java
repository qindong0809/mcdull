package io.gitee.dqcer.mcdull.framework.base.dto;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.support.Paged;

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
public class PagedDTO extends KeywordDTO implements Paged {

    /**
     * 每页记录数
     */
    @NotNull
    @Max(value = 100000)
    @Min(value = 1)
    protected Integer pageSize;

    /**
     * 当前页数
     */
    @NotNull
    @Min(value = 1)
    protected Integer pageNum;

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
    public Integer getPageSize() {
        if (this.notNeedPaged != null) {
            if (notNeedPaged) {
                return GlobalConstant.EXCEL_EXPORT_MAX_ROW;
            }
        }
        return Convert.toInt(pageSize, GlobalConstant.Number.NUMBER_20);
    }

    public PagedDTO setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public Integer getPageNum() {
        return Convert.toInt(pageNum, GlobalConstant.Number.NUMBER_1);
    }

    public PagedDTO setPageNum(Integer pageNum) {
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
