package io.gitee.dqcer.mcdull.system.facade.model.bo;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.facade.model.enums.FormItemControlTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态字段 BO（共享给业务模块）
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
    @SuppressWarnings("unchecked")
    public DynamicFieldBO(String code, String name, Boolean required, FormItemControlTypeEnum controlTypeEnum, Class clazz) {
        this(code, name, required, controlTypeEnum);
        List<IEnum<Integer>> list = IEnum.getAll(clazz);
        this.extraObj = list;
        this.dropdownList = list.stream().map(IEnum::getText).collect(Collectors.toList());
    }
}
