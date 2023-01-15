package ${package.Controller};

import com.dqcer.framework.base.annotation.Authorized;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.dto.StatusDTO;
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

import javax.annotation.Resource;

/**
* ${table.comment!} 前端控制器
*
* @author ${author}
* @version ${date}
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
    @Authorized("${cfg.modelName}:base:save")
    @PostMapping("base/save")
    public Result<Long> save(@RequestBody @Validated(value = {ValidGroup.Add.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.save(dto);
    }

    /**
    * 单条详情数据
    *
    * @param dto dto
    * @return {@link Result<${cfg.voName}>}
    */
    @GetMapping("base/detail")
    public Result<${cfg.voName}> detail(@Validated(value = {ValidGroup.One.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.detail(dto);
    }

    /**
    * 更新数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("${cfg.modelName}:base:update")
    @PutMapping("base/update")
    public Result<Long> update(@RequestBody @Validated(value = {ValidGroup.Update.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.update(dto);
    }

    /**
    * 状态更新
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("${cfg.modelName}:base:status")
    @PutMapping("base/status")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.updateStatus(dto);
    }

    /**
    * 删除数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("${cfg.modelName}:base:delete")
    @PostMapping("base/delete")
    public Result<Long> delete(@RequestBody @Validated(value = {ValidGroup.Delete.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.delete(dto);
    }

    /**
    * 查询分页数据
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @GetMapping("base/list")
    public Result<PagedVO<${cfg.voName}>> listByPage(@Validated(value = {ValidGroup.Paged.class}) ${cfg.dtoName} dto){
        return ${(cfg.serviceName?substring(1))?uncap_first}.listByPage(dto);
    }

}
