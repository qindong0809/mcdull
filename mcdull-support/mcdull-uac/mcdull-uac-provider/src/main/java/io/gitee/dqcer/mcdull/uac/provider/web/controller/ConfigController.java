package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IConfigService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
* 系统配置 控制器
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@RequestMapping("/sys-config")
public class ConfigController {

    @Resource
    private IConfigService configService;

    @Operation(summary = "Query")
    @PostMapping("/config/query")
    @SaCheckPermission("support:config:query")
    public Result<PagedVO<ConfigVO>> query(@RequestBody @Valid ConfigQueryDTO queryDTO) {
        return Result.success(configService.queryPage(queryDTO));
    }

    @Operation(summary = "Add")
    @PostMapping("/config/add")
    @SaCheckPermission("support:config:add")
    public Result<Boolean> add(@RequestBody @Valid ConfigAddDTO configAddDTO) {
        configService.add(configAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "Update")
    @PostMapping("/config/update")
    @SaCheckPermission("support:config:update")
    public Result<Boolean> update(@RequestBody @Valid ConfigUpdateDTO updateDTO) {
        configService.update(updateDTO);
        return Result.success(true);
    }

    @Operation(summary = "Delete")
    @PostMapping("/config/delete")
    @SaCheckPermission("support:config:delete")
    public Result<Boolean> delete(@RequestBody List<Long> configIdList) {
        configService.delete(configIdList);
        return Result.success(true);
    }
}
