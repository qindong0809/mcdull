package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IConfigService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
* 部门 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/config")
public class ConfigController {

    @Resource
    private IConfigService configService;


    /**
    * 列表查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Authorized("system:config:list")
    @GetMapping("list")
    public Result<PagedVO<ConfigVO>> list(@Validated(value = {ValidGroup.Paged.class}) ConfigLiteDTO dto){
        return configService.list(dto);
    }

    @Authorized("system:config:add")
    @PostMapping
    public Result<Long> add(@Validated @RequestBody ConfigAddDTO dto){
        return configService.add(dto);
    }

    @Authorized("system:config:edit")
    @PutMapping
    public Result<Long> edit(@Validated @RequestBody ConfigEditDTO dto){
        return configService.edit(dto);
    }

    @Authorized("system:config:remove")
    @DeleteMapping("{id}")
    public Result<Long> remove(@PathVariable(value = "id") Long id){
        return configService.remove(id);
    }

    /**
     * 单个详情
     *
     * @param id id
     * @return {@link Result}
     */
    @Authorized("system:config:query")
    @GetMapping("{id}")
    public Result<ConfigVO> detail(@PathVariable Long id) {
        return configService.detail(id);
    }



}
