package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 * 删除枚举 针对数据库 0/false 1/true
 *
 * @author dqcer
 * @since 2022/07/26
 */
public enum DelFlayEnum implements IEnum<Integer> {

    NORMAL(0, "正常"),

    DELETED(1, "已删除")
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
