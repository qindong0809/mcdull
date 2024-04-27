package io.gitee.dqcer.mcdull.framework.base.support;

/**
 * 分页接口
 *
 * @author dqcer
 * @since 2022/07/26
 */
public interface Paged {

    /**
     * 当前页
     *
     * @return {@link Long}
     */
    Integer getPageNum();


    /**
     * 每页数量
     *
     * @return {@link Long}
     */
    Integer getPageSize();

}
