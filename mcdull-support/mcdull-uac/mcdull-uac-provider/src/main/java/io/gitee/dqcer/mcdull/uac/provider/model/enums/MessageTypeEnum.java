package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * MessageTypeEnum
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum MessageTypeEnum implements IEnum<Integer> {

    MAIL(1, "站内信"),

    ORDER(2, "订单"),
    ;


    MessageTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
