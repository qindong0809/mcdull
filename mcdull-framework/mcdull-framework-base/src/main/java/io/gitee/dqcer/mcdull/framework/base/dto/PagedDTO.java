package io.gitee.dqcer.mcdull.framework.base.dto;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.support.Paged;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


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
    protected String sortField;

    /**
     * 排序方式 asc / desc
     */
    protected String sortOrder;

    public boolean isAsc() {
        return CharSequenceUtil.equalsIgnoreCase(GlobalConstant.PageSort.ASC, sortOrder);
    }


    @Override
    public String toString() {
        return "PagedDTO{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
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

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
