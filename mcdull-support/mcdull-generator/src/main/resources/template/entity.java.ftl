package ${cfg.apiEntity};

import com.baomidou.mybatisplus.annotation.TableName;
import ${cfg.baseEntity};
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * ${table.comment!} 实体类
 *
 * @author ${author}
 * @since ${date}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("${table.name}")
public class ${cfg.entityName} extends BaseEntity<Long> {

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if "id" != field.name >
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
   /**
    * ${field.comment}
    */
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
