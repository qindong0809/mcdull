package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 * 排序枚举
 *
 * @author dqcer
 * @since 2024/01/29
 */

public enum SortOrderEnum implements IEnum<String>{

    DESC("desc", ""),

    ASC("asc", "");

    SortOrderEnum(String code, String text) {
        init(code, text);
    }

}
