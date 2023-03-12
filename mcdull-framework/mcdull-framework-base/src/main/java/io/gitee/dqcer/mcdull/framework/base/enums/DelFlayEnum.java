package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 * 删除枚举 针对数据库 0/false 1/true
 *
 * @author dqcer
 * @since 2022/07/26
 */
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
        if (value == null) {
            throw new IllegalArgumentException(String.format("invalid value , only %s is allowed", IEnum.getCodes(DelFlayEnum.class)));
        }
        return IEnum.getByCode(DelFlayEnum.class, value);
    }
}
