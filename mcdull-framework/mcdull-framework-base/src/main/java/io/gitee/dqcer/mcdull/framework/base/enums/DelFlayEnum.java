package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 * 删除枚举 针对数据库 0/false 1/true
 *
 * @author dqcer
 * @version 2022/07/26
 */
public enum DelFlayEnum implements IEnum<Boolean> {
    /**
     * 1 = 正常
     */
    NORMAL(false, "正常"),

    /**
     * 2 = 已删除
     */
    DELETED(true, "已删除")
    ;

    DelFlayEnum(Boolean code, String text) {
        init(code, text);
    }

    public static DelFlayEnum toEnum(Boolean value) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("invalid value , only %s is allowed", IEnum.getCodes(DelFlayEnum.class)));
        }
        if (value) {
            return DelFlayEnum.DELETED;
        }
        return DelFlayEnum.NORMAL;
    }
}
