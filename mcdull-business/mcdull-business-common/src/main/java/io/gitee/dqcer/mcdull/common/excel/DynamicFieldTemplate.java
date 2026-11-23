package io.gitee.dqcer.mcdull.common.excel;

import lombok.Data;

import java.util.List;

/**
 * 动态字段（Excel 读取侧模板）。
 * <p>
 * controlType 用 String code，使 common 不依赖任何业务枚举。
 * 取值由调用方按业务枚举的 code 传入（如 "number"/"date"/"datetime"）。
 *
 * @author dqcer
 * @since 2024/12/16
 */
@Data
public class DynamicFieldTemplate {

    private String code;

    private String name;

    private Boolean required;

    private String controlType;

    private String dataType;

    private String dateformat;

    private List<String> dropdownList;

    private Object extraObj;

    public DynamicFieldTemplate(String code, String name, Boolean required, String controlType) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.controlType = controlType;
    }
}
