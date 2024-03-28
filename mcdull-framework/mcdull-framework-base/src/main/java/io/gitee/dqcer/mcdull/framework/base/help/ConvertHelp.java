package io.gitee.dqcer.mcdull.framework.base.help;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 转换帮助
 *
 * @author dqcer
 * @since 2024/03/28
 */
@SuppressWarnings("unused")
public class ConvertHelp {


    public static <T, E> List<T> convert(List<E> list, Supplier<T> supplier) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<T> newList = new ArrayList<>();
        for (E e : list) {
            T t = supplier.get();
            if (ObjUtil.isNotNull(t)) {
                newList.add(t);
            }
        }
        return newList;
    }
}
