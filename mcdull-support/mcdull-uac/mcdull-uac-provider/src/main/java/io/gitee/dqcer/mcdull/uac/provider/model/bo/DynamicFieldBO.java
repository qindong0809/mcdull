package io.gitee.dqcer.mcdull.uac.provider.model.bo;

import io.gitee.dqcer.mcdull.uac.provider.model.enums.FormItemControlTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 动态字段
 *
 * @author dqcer
 * @since 2024/12/16
 */
@Data
public class DynamicFieldBO {

    private String code;

    private String name;

    private Boolean required;

    private FormItemControlTypeEnum controlTypeEnum;

    private String dataType;

    private String dateformat;

    private List<String> dropdownList;

    private Object extraObj;

    public DynamicFieldBO(String code, String name, Boolean required, FormItemControlTypeEnum controlTypeEnum) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.controlTypeEnum = controlTypeEnum;
    }
}
