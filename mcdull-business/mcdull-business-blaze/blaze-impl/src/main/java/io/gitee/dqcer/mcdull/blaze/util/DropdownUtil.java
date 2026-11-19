package io.gitee.dqcer.mcdull.blaze.util;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.facade.model.bo.DynamicFieldBO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举下拉项 util
 * <p>
 * facade 的 DynamicFieldBO 只接收已解析好的 {code,text}，把字典枚举翻成下拉项这件事留在实现方：
 * 这里继续走 {@link IEnum#getAll}，从而保留它对 @Deprecated 枚举项的过滤与 getText 的 i18n 回落。
 *
 * @author dqcer
 * @since 2026-11-19
 */
public class DropdownUtil {

    private DropdownUtil() {
    }

    /**
     * 字典枚举转下拉项
     *
     * @param clazz 枚举类
     * @return {@link DynamicFieldBO.Option} 列表
     */
    public static <T> List<DynamicFieldBO.Option<T>> options(Class<? extends IEnum<T>> clazz) {
        return IEnum.getAll(clazz).stream()
                .map(item -> new DynamicFieldBO.Option<>(item.getCode(), item.getText()))
                .collect(Collectors.toList());
    }
}
