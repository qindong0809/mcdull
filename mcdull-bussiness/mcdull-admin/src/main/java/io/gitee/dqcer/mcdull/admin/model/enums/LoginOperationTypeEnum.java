package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录操作枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum LoginOperationTypeEnum implements IEnum<String> {

    /**
     * 登录
     */
    LOGIN("1", "登录"),

    /**
     * 注销
     */
    LOGOUT("2", "注销"),

    ;

    LoginOperationTypeEnum(String code, String text) {
        init(code, text);
    }

}
