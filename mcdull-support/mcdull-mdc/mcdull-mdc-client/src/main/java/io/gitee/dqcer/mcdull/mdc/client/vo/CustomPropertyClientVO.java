package io.gitee.dqcer.mcdull.mdc.client.vo;

/**
 *
 *
 * @author dqcer
 * @since 2024/03/26
 */
public class CustomPropertyClientVO {

    private Integer id;

    private String code;

    private String name;

    private String propertyValue;

    private String remark;

    public Integer getId() {
        return id;
    }

    public CustomPropertyClientVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CustomPropertyClientVO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomPropertyClientVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public CustomPropertyClientVO setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public CustomPropertyClientVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
