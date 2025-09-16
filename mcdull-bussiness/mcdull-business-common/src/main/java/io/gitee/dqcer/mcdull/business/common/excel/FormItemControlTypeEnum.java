package io.gitee.dqcer.mcdull.business.common.excel;



/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum FormItemControlTypeEnum {

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

    private String code;
    private String text;

    FormItemControlTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }
    public String getCode() {
        return code;
    }
    public String getText() {
        return text;
    }

}
