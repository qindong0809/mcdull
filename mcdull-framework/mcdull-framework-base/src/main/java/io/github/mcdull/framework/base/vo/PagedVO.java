package io.gitee.dqcer.framework.base.vo;

import io.gitee.dqcer.framework.base.dto.IPaged;

import java.util.List;

/**
 * 分页对象
 *
 * @author dqcer
 * @version 2022/05/06
 */
@SuppressWarnings("unused")
public class PagedVO<T> implements IPaged, VO {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 每页记录数
     */
    private Long pageSize;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 当前页数
     */
    private Long currentPage;
    /**
     * 列表数据
     */
    private List<T> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currentPage   当前页数
     */
    public PagedVO(List<T> list, Long totalCount, Long pageSize, Long currentPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage = (long) (int) Math.ceil((double) totalCount / pageSize);
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", currentPage=" + currentPage +
                ", list=" + list +
                '}';
    }
}
