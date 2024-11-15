package io.gitee.dqcer.mcdull.framework.base.dto;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.support.Paged;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Max(value = 100000)
    @Min(value = 1)
    protected Integer pageSize = 10;

    /**
     * 当前页数
     */
    @Min(value = 1)
    protected Integer pageNum = 1;

    /**
     * 排序字段信息
     */
    protected List<OrderItem> orders;


    @Override
    public String toString() {
        return new StringJoiner(", ", PagedDTO.class.getSimpleName() + "[", "]")
                .add("pageSize=" + pageSize)
                .add("pageNum=" + pageNum)
                .add("orders=" + orders)
                .add("keyword='" + keyword + "'")
                .toString();
    }

    @Override
    public Integer getPageSize() {
        return Convert.toInt(pageSize);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getPageNum() {
        return Convert.toInt(pageNum);
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public PagedDTO setOrders(List<OrderItem> orders) {
        this.orders = orders;
        return this;
    }
}
