package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 状态枚举
 *
 * @author dqcer
 * @since 2024/03/01
 */
public enum StatusEnum implements IEnum<String> {

    /**
     * 启用
     */
    ENABLE("0", "启动"),

    /**
     * 禁用
     */
    DISABLE("1", "停用");


    StatusEnum(String code, String text) {
        init(code, text);
    }

    @SuppressWarnings("unused")
    public static StatusEnum toEnum(String value) {
        switch (value) {
            case "0":
                return StatusEnum.ENABLE;
            case "1":
                return StatusEnum.DISABLE;
            default:
                throw new IllegalArgumentException("invalid value , only ['1', '2'] is allowed");
        }
    }

    public static StatusEnum toEnum(Integer value) {
        switch (value) {
            case 0:
                return StatusEnum.ENABLE;
            case 1:
                return StatusEnum.DISABLE;
            default:
                throw new IllegalArgumentException("invalid value , only ['1', '2'] is allowed");
        }
    }
}
