package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * email 类型 enum
 *
 * @author dqcer
 * @since 2024/11/19
 */
public enum EmailTypeEnum implements IEnum<Integer> {

    FORGET_PASSWORD(1, "忘记密码"),

    CREATE_ACCOUNT(2, "创建账号"),
    ;



    EmailTypeEnum(Integer value, String desc) {
        this.init(value, desc);
    }

}
