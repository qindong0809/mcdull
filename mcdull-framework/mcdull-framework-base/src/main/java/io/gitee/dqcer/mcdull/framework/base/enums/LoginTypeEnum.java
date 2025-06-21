package io.gitee.dqcer.mcdull.framework.base.enums;


/**
 * 登录类型枚举
 * @author dqcer
 * @since 2025/06/17
 */
public enum LoginTypeEnum implements IEnum<Integer> {

    ADMINISTRATOR(0, "Administrator"),

    USER(1, "User")
    ;

    LoginTypeEnum(Integer code, String text) {
        init(code, text);
    }

    public static LoginTypeEnum toEnum(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("invalid value , only %s is allowed", IEnum.getCodes(LoginTypeEnum.class)));
        }
        return IEnum.getByCode(LoginTypeEnum.class, value);
    }
}
