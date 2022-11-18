package com.dqcer.framework.base.enums;

import com.dqcer.framework.base.dict.IDict;

/**
 * 性枚举
 *
 * @author dqcer
 * @version 2022/07/26
 */
@SuppressWarnings("unused")
public enum SexEnum implements IDict<Integer> {

    /**
     * 未知
     */
    UNKNOWN(0, "未知"),
    /**
     * 男
     */
    MALE(1, "男"),

    /**
     * 女
     */
    FEMALE(2, "女"),

    /**
     * 未说明
     */
    UNSPECIFIED(9, "未说明");

    SexEnum(Integer code, String text) {
        init(code, text);
    }

    public static SexEnum toEnum(int code) {
        switch (code) {
            case 0:
                return SexEnum.UNKNOWN;
            case 1:
                return SexEnum.MALE;
            case 2:
                return SexEnum.FEMALE;
            case 9:
                return SexEnum.UNSPECIFIED;
            default:
                throw new IllegalArgumentException("invalid value , only [0, 1, 2, 9] is allowed");
        }
    }
}
