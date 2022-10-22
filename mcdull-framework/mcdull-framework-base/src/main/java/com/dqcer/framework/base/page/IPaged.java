package com.dqcer.framework.base.page;

/**
 * 分页接口
 *
 * @author dqcer
 * @date 2022/07/26
 */
@SuppressWarnings("unused")
public interface IPaged {

    /**
     * 当前页
     *
     * @return {@link Long}
     */
    Long getCurrPage();


    /**
     * 每页数量
     *
     * @return {@link Long}
     */
    Long getPageSize();

    // TODO: 2022/4/3 排序处理

}
