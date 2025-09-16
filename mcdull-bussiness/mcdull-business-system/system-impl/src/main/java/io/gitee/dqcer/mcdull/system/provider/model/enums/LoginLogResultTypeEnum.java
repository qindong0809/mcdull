package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum LoginLogResultTypeEnum implements IEnum<Integer> {

    /**
     * 登录成功
     */
    LOGIN_SUCCESS(0, "登录成功"),

    /**
     * 登录失败
     */
    LOGIN_FAIL(1, "登录失败"),

    /**
     * 退出状态
     */
    LOGIN_OUT(2, "退出登录");
    ;

    LoginLogResultTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
