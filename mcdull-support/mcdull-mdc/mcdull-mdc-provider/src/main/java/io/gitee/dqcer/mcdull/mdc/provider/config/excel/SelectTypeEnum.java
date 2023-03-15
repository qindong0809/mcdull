package io.gitee.dqcer.mcdull.mdc.provider.config.excel;


/**
 * 下拉类型的枚举
 *
 * @author dqcer
 * @since 2022/11/20 22:11:86
 */
public enum SelectTypeEnum {


    /**
     * 状态
     */
    STATUS("1", "状态"),

    ;


    private String code;
    private String name;


    private SelectTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}