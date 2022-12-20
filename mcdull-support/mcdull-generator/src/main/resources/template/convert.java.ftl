package ${cfg.apiConvert};

import ${cfg.apiVo}.${cfg.voName};
import ${cfg.apiDto}.${cfg.dtoName};
import ${cfg.apiEntity}.${cfg.entityName};

/**
* ${table.comment!} 对象转换类
*
* @author ${author}
* @version ${date}
*/
public class ${cfg.convertName} {

   /**
    * ${cfg.dtoName}转换为${cfg.entityName}
    *
    * @param item ${cfg.dtoName}
    * @return {@link ${cfg.entityName}}
    */
    public static ${cfg.entityName} convertTo${cfg.entityName}(${cfg.dtoName} item){
        if (item == null){
            return null;
        }
        ${cfg.entityName} entity = new ${cfg.entityName}();
        <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
        entity.set${field.capitalName}(item.${getprefix}${field.capitalName}());
        </#list>

        return entity;
    }


    /**
    * ${cfg.entityName}${cfg.voName}
    *
    * @param item ${cfg.entityName}
    * @return {@link ${cfg.voName}}
    */
    public static ${cfg.voName} convertTo${cfg.voName}(${cfg.entityName} item){
        if (item == null){
            return null;
        }
        ${cfg.voName} vo = new ${cfg.voName}();
        <#list table.fields as field>
            <#if field.propertyType == "boolean">
                <#assign getprefix="is"/>
            <#else>
                <#assign getprefix="get"/>
            </#if>
        vo.set${field.capitalName}(item.${getprefix}${field.capitalName}());
        </#list>

        return vo;
    }
    private ${cfg.convertName}() {
    }
}