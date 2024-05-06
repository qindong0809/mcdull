package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 删除类型
 *
 */
public enum CodeFrontComponentEnum implements IEnum<String> {

    INPUT("Input", "输入框"),

    INPUT_NUMBER("InputNumber", "数字输入框"),

    TEXTAREA("Textarea", " 文本"),

    BOOLEAN_SELECT("BooleanSelect", "布尔下拉框"),

    ENUM_SELECT("SmartEnumSelect", "枚举下拉"),

    DICT_SELECT("DictSelect", "字典下拉"),

    DATE("Date", "日期选择"),

    DATE_TIME("DateTime", "时间选择"),

    FILE_UPLOAD("FileUpload", "文件上传");

    CodeFrontComponentEnum(String value, String desc) {
        init(value, desc);
    }

    public static CodeFrontComponentEnum toEnum(String code) {
        switch (code) {
            case "Input":
                return CodeFrontComponentEnum.INPUT;
            case "InputNumber":
                return CodeFrontComponentEnum.INPUT_NUMBER;
            case "Textarea":
                return CodeFrontComponentEnum.TEXTAREA;
            case "BooleanSelect":
                return CodeFrontComponentEnum.BOOLEAN_SELECT;
            case "SmartEnumSelect":
                return CodeFrontComponentEnum.ENUM_SELECT;
            case "DictSelect":
                return CodeFrontComponentEnum.DICT_SELECT;
            case "Date":
                return CodeFrontComponentEnum.DATE;
            case "DateTime":
                return CodeFrontComponentEnum.DATE_TIME;
            case "FileUpload":
                return CodeFrontComponentEnum.FILE_UPLOAD;
            default:
                throw new IllegalArgumentException("invalid value , only [Input, InputNumber, Textarea, BooleanSelect, SmartEnumSelect, DictSelect, Date...] is allowed");
        }
    }

}
