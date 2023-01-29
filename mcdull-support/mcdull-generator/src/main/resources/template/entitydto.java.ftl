package ${cfg.apiDto};

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
* ${table.comment!} 接收客户端参数
*
* @author ${author}
* @since ${date}
*/
public class ${cfg.dtoName} extends PagedDTO {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
     <#if "id" == field.name>
     <#if field.comment!?length gt 0>
     /**
      * ${field.comment}
      */
     </#if>
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class})
     private ${field.propertyType} ${field.propertyName};
     </#if>
    <#if "status" == field.name>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
     @NotNull(groups = {ValidGroup.Status.class})
     private ${field.propertyType} ${field.propertyName};
    </#if>
    <#if "status" != field.name && "id" != field.name>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    <#if "Long" == field.propertyType>
    @NotNull(groups = {ValidGroup.Insert.class})
    private ${field.propertyType} ${field.propertyName};
    </#if>
    <#if "Integer" == field.propertyType>
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private ${field.propertyType} ${field.propertyName};
    </#if>
    <#if "String" == field.propertyType>
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private ${field.propertyType} ${field.propertyName};
    </#if>
    <#if "Date" == field.propertyType>
    @NotNull(groups = {ValidGroup.Insert.class})
    private ${field.propertyType} ${field.propertyName};
    </#if>
    </#if>

     </#if>
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

    <#if entityBuilderModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    </#if>
        this.${field.propertyName} = ${field.propertyName};
    <#if entityBuilderModel>
        return this;
    </#if>
    }
    </#list>
</#if>

<#if entityColumnConstant>
    <#list table.fields as field>
        public static final String ${field.name?upper_case} = "${field.name}";

    </#list>
</#if>
<#if activeRecord>
    @Override
    protected Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

</#if>
<#if !entityLombokModel>
    @Override
    public String toString() {
    return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
            "${field.propertyName}=" + ${field.propertyName} +
        <#else>
            ", ${field.propertyName}=" + ${field.propertyName} +
        </#if>
    </#list>
    "}";
    }
</#if>
}