package com.dqcer.framework.base.dto;

/**
 * 分页接口
 *
 * @author dqcer
 * @version 2022/07/26
 */
public interface IPaged {

    /**
     * 当前页
     *
     * @return {@link Long}
     */
    Long getCurrentPage();


    /**
     * 每页数量
     *
     * @return {@link Long}
     */
    Long getPageSize();

    // TODO: 2022/4/3 排序处理

}
