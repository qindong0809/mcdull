package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 代码前端组件枚举
 *
 * @author dqcer
 * @since 2024/06/03
 */
public enum CodeFrontComponentEnum implements IEnum<String> {

    /**
     * 输入框
     */
    INPUT("Input", "输入框"),

    /**
     * 数字输入框
     */
    INPUT_NUMBER("InputNumber", "数字输入框"),

    /**
     * 文本
     */
    TEXTAREA("Textarea", " 文本"),

    /**
     * 布尔下拉框
     */
    BOOLEAN_SELECT("BooleanSelect", "布尔下拉框"),

    /**
     * 枚举下拉
     */
    ENUM_SELECT("SmartEnumSelect", "枚举下拉"),

    /**
     * 字典下拉
     */
    DICT_SELECT("DictSelect", "字典下拉"),

    /**
     * 日期选择
     */
    DATE("Date", "日期选择"),

    /**
     * 时间选择
     */
    DATE_TIME("DateTime", "时间选择"),

    /**
     * 文件上传
     */
    FILE_UPLOAD("FileUpload", "文件上传");

    CodeFrontComponentEnum(String value, String desc) {
        init(value, desc);
    }

}
