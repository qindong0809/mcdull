package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 * 状态枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
public enum StatusEnum implements IEnum<String> {

    /**
     * 启用
     */
    ENABLE("1", "启动"),

    /**
     * 禁用
     */
    DISABLE("2", "停用");


    StatusEnum(String code, String text) {
        init(code, text);
    }

    @SuppressWarnings("unused")
    public static StatusEnum toEnum(String value) {
        switch (value) {
            case "1":
                return StatusEnum.ENABLE;
            case "2":
                return StatusEnum.DISABLE;
            default:
                throw new IllegalArgumentException("invalid value , only ['1', '2'] is allowed");
        }
    }

    public static StatusEnum toEnum(Integer value) {
        switch (value) {
            case 1:
                return StatusEnum.ENABLE;
            case 2:
                return StatusEnum.DISABLE;
            default:
                throw new IllegalArgumentException("invalid value , only ['1', '2'] is allowed");
        }
    }
}
