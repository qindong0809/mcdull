package ${cfg.apiVo};

import ${cfg.baseVO};
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* ${table.comment!} 返回客户端值
*
* @author ${author}
* @since ${date}
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ${cfg.voName} implements VO {

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
<#------------  END 字段循环遍历  ---------->

}