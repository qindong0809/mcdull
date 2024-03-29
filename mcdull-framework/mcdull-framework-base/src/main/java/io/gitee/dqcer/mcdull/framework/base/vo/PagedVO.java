package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.support.Paged;
import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.List;

/**
 * 分页对象
 *
 * @author dqcer
 * @since 2022/05/06
 */
@SuppressWarnings("unused")
public class PagedVO<T> implements Paged, VO {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 当前页数
     */
    private Integer currentPage;
    /**
     * 列表数据
     */
    private List<T> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param total 总记录数
     * @param pageSize   每页记录数
     * @param currentPage   当前页数
     */
    public PagedVO(List<T> list, Integer total, Integer pageSize, Integer currentPage) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage =  (int) Math.ceil((double) total / pageSize);
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }


    /**
     * 当前页
     *
     * @return {@link Long}
     */
    @Override
    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "totalCount=" + total +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", page=" + currentPage +
                ", list=" + list +
                '}';
    }
}
