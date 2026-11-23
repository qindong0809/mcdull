package io.gitee.dqcer.mcdull.system.facade.model.bo;

import io.gitee.dqcer.mcdull.framework.base.support.ExcelField;
import io.gitee.dqcer.mcdull.system.facade.model.enums.FormItemControlTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态字段 BO（共享给业务模块）
 */
@Data
public class DynamicFieldBO implements ExcelField {

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

    /**
     * 带下拉项的字段：下拉项由调用方解析好传进来，本类不认识任何字典框架类型。
     *
     * @param code             字段编码
     * @param name             字段名称
     * @param required         是否必填
     * @param controlTypeEnum  控件类型
     * @param options          下拉项，同时作为 extraObj 输出给前端
     */
    public DynamicFieldBO(String code, String name, Boolean required, FormItemControlTypeEnum controlTypeEnum,
                          List<? extends Option<?>> options) {
        this(code, name, required, controlTypeEnum);
        this.extraObj = options;
        this.dropdownList = options.stream().map(Option::getText).collect(Collectors.toList());
    }

    /**
     * 下拉项，序列化为 {code,text}
     */
    @Data
    public static class Option<T> {

        private final T code;

        private final String text;
    }

    @Override
    public boolean isSelectable() {
        return controlTypeEnum == FormItemControlTypeEnum.SELECT
                || controlTypeEnum == FormItemControlTypeEnum.RADIO
                || controlTypeEnum == FormItemControlTypeEnum.SWITCH
                || controlTypeEnum == FormItemControlTypeEnum.CHECKBOX;
    }
}
