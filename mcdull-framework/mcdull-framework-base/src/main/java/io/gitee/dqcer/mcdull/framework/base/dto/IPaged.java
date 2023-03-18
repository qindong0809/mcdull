package io.gitee.dqcer.mcdull.framework.base.dto;

/**
 * 分页接口
 *
 * @author dqcer
 * @since 2022/07/26
 */
public interface IPaged {

    /**
     * 当前页
     *
     * @return {@link Long}
     */
    Long getPageNum();


    /**
     * 每页数量
     *
     * @return {@link Long}
     */
    Long getPageSize();

}
