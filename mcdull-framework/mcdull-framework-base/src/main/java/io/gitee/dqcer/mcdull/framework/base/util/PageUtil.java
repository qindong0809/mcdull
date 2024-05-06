package io.gitee.dqcer.mcdull.framework.base.util;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.gitee.dqcer.mcdull.framework.base.support.Paged;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.Collections;
import java.util.List;

/**
 * 分页工具
 *
 * @author dqcer
 * @since 2022/05/06
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
    public static <T> PagedVO<T> toPage(List<T> list, IPage<?> iPage) {
        return new PagedVO<>(list, Convert.toInt(iPage.getTotal()),
                Convert.toInt(iPage.getSize()), Convert.toInt(iPage.getCurrent()));
    }

    public static <T> PagedVO<T> empty(Paged iPage) {
        return new PagedVO<>(Collections.emptyList(), 0,
                Convert.toInt(iPage.getPageSize()), Convert.toInt(iPage.getPageNum()));
    }
}
