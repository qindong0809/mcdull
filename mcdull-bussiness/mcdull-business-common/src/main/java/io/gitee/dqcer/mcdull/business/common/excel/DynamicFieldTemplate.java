package io.gitee.dqcer.mcdull.business.common.excel;

import io.gitee.dqcer.mcdull.framework.base.enums.FormItemControlTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 动态字段
 *
 * @author dqcer
 * @since 2024/12/16
 */
@Data
public class DynamicFieldTemplate {

    private String code;

    private String name;

    private Boolean required;

    private FormItemControlTypeEnum controlTypeEnum;

    private String dataType;

    private String dateformat;

    private List<String> dropdownList;

    private Object extraObj;

    public DynamicFieldTemplate(String code, String name, Boolean required, FormItemControlTypeEnum controlTypeEnum) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.controlTypeEnum = controlTypeEnum;
    }
}
