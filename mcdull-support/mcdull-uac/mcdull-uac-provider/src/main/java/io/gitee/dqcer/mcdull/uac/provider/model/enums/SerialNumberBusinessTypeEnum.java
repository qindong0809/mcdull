package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * business type类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum SerialNumberBusinessTypeEnum implements IEnum<Integer> {

    FEEDBACK(1, "意见反馈"),

    ;


    SerialNumberBusinessTypeEnum(Integer code, String text) {
        init(code, text);
    }

    public static SerialNumberBusinessTypeEnum toEnum(Integer code) {
        return IEnum.getByCode(SerialNumberBusinessTypeEnum.class, code);
    }

}
