package com.dqcer.framework.base.enums;

/**
 * 删除枚举
 *
 * @author dqcer
 * @version 2022/07/26
 */
@SuppressWarnings("unused")
public enum DelFlayEnum implements IEnum<Integer> {
    /**
     * 1 = 正常
     */
    NORMAL(1, "正常"),

    /**
     * 2 = 已删除
     */
    DELETED(2, "已删除")
    ;

    DelFlayEnum(Integer code, String text) {
        init(code, text);
    }

    public static DelFlayEnum toEnum(Integer value) {
        switch (value) {
            case 1:
                return DelFlayEnum.NORMAL;
            case 2:
                return DelFlayEnum.DELETED;
            default:
                throw new IllegalArgumentException("invalid value , only [1, 2] is allowed");
        }
    }
}
