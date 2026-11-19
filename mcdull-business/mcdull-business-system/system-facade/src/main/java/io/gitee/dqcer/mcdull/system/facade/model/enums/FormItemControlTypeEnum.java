package io.gitee.dqcer.mcdull.system.facade.model.enums;


import lombok.Getter;

import java.util.Arrays;

/**
 * 表单控件类型枚举（共享给业务模块）
 * <p>
 * 纯 POJO 枚举，不依赖 starter-web：控件类型是契约，翻译与字典池是实现方的事。
 */
@Getter
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

    private final String code;

    private final String text;

    FormItemControlTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * 按 code 取枚举，取不到返回 null
     *
     * @param code 编码
     * @return {@link FormItemControlTypeEnum}
     */
    public static FormItemControlTypeEnum of(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findAny().orElse(null);
    }
}
