package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum SerialNumberFormatTypeEnum implements IEnum<String> {

    YYYY("{yyyy}", "年"),

    MM("{mm}", "月"),

    DD("{dd}", "日"),


    ;


    SerialNumberFormatTypeEnum(String code, String text) {
        init(code, text);
    }

}
