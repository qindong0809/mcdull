package com.dqcer.framework.base.page;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 分页工具
 *
 * @author dqcer
 * @date 2022/05/06
 */
@SuppressWarnings("unused")
public class PageUtil {

    /**
     * 禁止实例化
     */
    private PageUtil(){
        throw new AssertionError();
    }

    /**
     * page 转换
     *
     * @param list  列表
     * @param iPage 我的页面
     * @return {@link Paged<T>}
     */
    public static <T> Paged<T> toPage(List<T> list, IPage iPage) {
        return new Paged(list, iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }
}
