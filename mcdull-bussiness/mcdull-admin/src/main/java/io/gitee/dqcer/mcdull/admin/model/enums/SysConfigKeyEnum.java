package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 系统配置key枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum SysConfigKeyEnum implements IEnum<String> {
    /**
     * 登录是否开启验证码
     */
    CAPTCHA("sys.account.captchaEnabled", "登录是否开启验证码"),

    ;

    SysConfigKeyEnum(String code, String text) {
        init(code, text);
    }

}
