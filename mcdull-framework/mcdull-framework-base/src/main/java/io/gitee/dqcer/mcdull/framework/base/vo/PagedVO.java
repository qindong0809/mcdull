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
    private Integer pageNum;
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
     * @param pageNum   当前页数
     */
    public PagedVO(List<T> list, Integer total, Integer pageSize, Integer pageNum) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalPage =  (int) Math.ceil((double) total / pageSize);
    }

    public PagedVO() {

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


    @Override
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
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
                ", pageNum=" + pageNum +
                ", list=" + list +
                '}';
    }
}
