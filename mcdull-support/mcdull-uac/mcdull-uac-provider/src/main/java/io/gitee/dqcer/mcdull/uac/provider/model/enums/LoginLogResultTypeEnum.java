package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum LoginLogResultTypeEnum implements IEnum<Integer> {

    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAIL(1, "登录失败"),
    LOGIN_OUT(2, "退出登录");
    ;

    LoginLogResultTypeEnum(Integer code, String text) {
        init(code, text);
    }

    public static LoginLogResultTypeEnum toEnum(Integer code) {
        return IEnum.getByCode(LoginLogResultTypeEnum.class, code);
    }

}
