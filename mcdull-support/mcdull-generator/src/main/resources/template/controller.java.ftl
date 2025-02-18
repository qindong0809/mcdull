package ${package.Controller};

import ${cfg.Authorized};
import ${cfg.IdDTO};
import ${cfg.PagedVO};
import ${cfg.ValidGroup};
import ${cfg.StatusDTO};
import ${cfg.PkDTO};
import ${package.Service}.${cfg.serviceName};
import ${cfg.apiVo}.${cfg.voName};
import ${cfg.apiDto}.${cfg.dtoName};
import ${cfg.result};
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
* ${table.comment!} 控制器
*
* @author ${author}
* @since ${date}
*/
@RestController
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
public class ${cfg.controller} {

    @Resource
    private ${cfg.serviceName} ${(cfg.serviceName?substring(1))?uncap_first};

    /**
    * 新增数据
    *
    * @param dto dto
    * @return {@link Result<Long> 返回新增主键}
    */
    @Authorized("${cfg.modelName}:${cfg.moduleCode}:insert")
    @PostMapping("insert")
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.insert(dto);
    }

    /**
    * 通过主键查询单条数据
    *
    * @param dto dto
    * @return {@link Result<${cfg.voName}>}
    */
    @GetMapping("detail")
    public Result<${cfg.voName}> detail(@Validated PkDTO dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.detail(dto.getId());
    }

    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("${cfg.modelName}:${cfg.moduleCode}:update")
    @PutMapping("update")
    public Result<Long> update(@RequestBody @Validated(value = {ValidGroup.Update.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.update(dto);
    }
<#list table.fields as field>
<#if "status" == field.name>
    /**
    * 状态更新
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("${cfg.modelName}:${cfg.moduleCode}:status")
    @PutMapping("status")
    public Result<Long> toggleActive(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.updateStatus(dto);
    }
</#if>
</#list>

    /**
    * 根据主键删除
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("${cfg.modelName}:${cfg.moduleCode}:delete")
    @PostMapping("delete")
    public Result<Long> deleteById(@RequestBody @Valid PkDTO dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.deleteById(dto.getId());
    }

    /**
    * 分页查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Authorized("${cfg.modelName}:${cfg.moduleCode}:view")
    @GetMapping("list")
    public Result<PagedVO<${cfg.voName}>> listByPage(@Validated(value = {ValidGroup.Paged.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.listByPage(dto);
    }
}
