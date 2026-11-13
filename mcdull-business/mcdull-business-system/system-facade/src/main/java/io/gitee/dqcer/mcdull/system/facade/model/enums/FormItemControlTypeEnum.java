package io.gitee.dqcer.mcdull.system.facade.model.enums;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 表单控件类型枚举（共享给业务模块）
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
