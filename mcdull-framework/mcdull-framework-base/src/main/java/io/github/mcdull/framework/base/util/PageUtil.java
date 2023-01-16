package io.gitee.dqcer.framework.base.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.gitee.dqcer.framework.base.vo.PagedVO;

import java.util.List;

/**
 * 分页工具
 *
 * @author dqcer
 * @version 2022/05/06
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
     * @return {@link PagedVO <T>}
     */
    public static <T> PagedVO<T> toPage(List<T> list, IPage iPage) {
        return new PagedVO(list, iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }
}
