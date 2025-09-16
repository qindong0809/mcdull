package io.gitee.dqcer.mcdull.framework.web.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 性别枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum SexEnum implements IEnum<Integer> {

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
        SexEnum sexEnum;
        switch (code) {
            case 0:
                sexEnum = SexEnum.UNKNOWN;
                break;
            case 1:
                sexEnum = SexEnum.MALE;
                break;
            case 2:
                sexEnum = SexEnum.FEMALE;
                break;
            case 9:
                sexEnum = SexEnum.UNSPECIFIED;
                break;
            default:
                throw new IllegalArgumentException("invalid value , only [0, 1, 2, 9] is allowed");
        }
        return sexEnum;
    }
}
