package io.gitee.dqcer.mcdull.framework.base.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.support.Paged;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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

    public static <T> PagedVO<T> toPage(IPage<T> iPage) {
        return new PagedVO<>(iPage.getRecords(), Convert.toInt(iPage.getTotal()),
                Convert.toInt(iPage.getSize()), Convert.toInt(iPage.getCurrent()));
    }

    public static <T, D extends Paged> PagedVO<T> empty(D iPage) {
        return new PagedVO<>(Collections.emptyList(), 0,
                Convert.toInt(iPage.getPageSize()), Convert.toInt(iPage.getPageNum()));
    }

    public static <T, D extends PagedDTO> PagedVO<T> ofSub(List<T> list, D pageDTO) {
        if (CollUtil.isEmpty(list)) {
            return empty(pageDTO);
        }
        Integer pageNum = pageDTO.getPageNum();
        Integer pageSize = pageDTO.getPageSize();
        int total = list.size();
        if (ObjectUtil.isNull(pageNum) || ObjectUtil.isNull(pageSize)) {
            throw new IllegalArgumentException("pageNum or pageSize is null");
        }
        int remainder = total % pageSize;
        int totalPage = remainder > GlobalConstant.Number.NUMBER_0
                ? total / pageSize + GlobalConstant.Number.NUMBER_1
                : total / pageSize;
        Integer currentPage = totalPage < pageNum ? totalPage : pageNum;
        int fromIndex = pageSize * (currentPage - GlobalConstant.Number.NUMBER_1);
        int toIndex = Math.min(pageSize * currentPage, total);
        List<T> subList = list.subList(fromIndex, toIndex);
        PagedVO<T> page = new PagedVO<>(subList, total, pageSize, currentPage);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotal(total);
        return page;
    }

    public static <T, D extends PagedDTO> PagedVO<T> ofSub1(List<T> list, D pageDTO) {
        int start = (pageDTO.getPageNum() - 1) * pageDTO.getPageSize();
        int total = list.size();
        int end = start + pageDTO.getPageSize();
        if (total <= end && total >= start) {
            end = total;
            return new PagedVO<>(list.subList(start, end), Convert.toInt(total), pageDTO.getPageSize(), pageDTO.getPageNum());
        }
        return empty(pageDTO);
    }

    public static <T, D extends PagedDTO> PagedVO<T> of(List<T> list, D dto) {
        return new PagedVO<>(list, list.size(), dto.getPageSize(), dto.getPageNum());
    }

    public static <D extends PagedDTO> void setMaxPageSize(D dto) {
        dto.setPageSize(Integer.MAX_VALUE);
        dto.setPageNum(GlobalConstant.Number.NUMBER_1);
    }

    public static <T> List<T> getList(PagedVO<T> pagedVO) {
        return CollUtil.isEmpty(pagedVO.getList()) ? Collections.emptyList() : pagedVO.getList();
    }

    public static <D extends PagedDTO, V> List<V> getAllList(D dto, Function<D, PagedVO<V>> function) {
        PageUtil.setMaxPageSize(dto);
        PagedVO<V> apply = function.apply(dto);
        return CollUtil.isEmpty(apply.getList()) ? Collections.emptyList() : apply.getList();
    }
}
