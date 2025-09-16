package io.gitee.dqcer.mcdull.business.common.excel;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum FormItemControlTypeEnum implements IEnum<String> {

    INPUT("input", "input"),
    TEXTAREA("textarea", "textarea"),
    SELECT("select", "select"),
    RADIO("radio", "radio"),
    SWITCH("switch", "switch"),
    CHECKBOX("checkbox", "checkbox"),
    DATE("date", "date"),
    TIME("time", "time"),
    DATETIME("datetime", "datetime"),
    NUMBER("number", "number"),
    FILE("file", "file"),
    ;


    FormItemControlTypeEnum(String code, String text) {
        init(code, text);
    }

}
