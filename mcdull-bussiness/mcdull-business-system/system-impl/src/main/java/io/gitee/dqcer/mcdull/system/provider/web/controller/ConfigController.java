package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ConfigInfoVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* 系统配置 控制器
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "系统配置")
@RequestMapping
public class ConfigController extends BasicController {

    @Resource
    private IConfigService configService;

    @Operation(summary = "分页查询")
    @PostMapping("/config/query")
    @SaCheckPermission("support:config:query")
    public Result<PagedVO<ConfigInfoVO>> query(@RequestBody @Valid ConfigQueryDTO queryDTO) {
        return Result.success(configService.queryPage(queryDTO));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:config:export")
    @PostMapping(value = "/system/config/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid ConfigQueryDTO dto) {
        super.locker(null, () -> configService.exportData(dto));
    }

    @Operation(summary = "添加")
    @PostMapping("/config/add")
    @SaCheckPermission("support:config:add")
    public Result<Boolean> add(@RequestBody @Valid ConfigAddDTO configAddDTO) {
        configService.add(configAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "导出附件")
    @SaCheckPermission("system:config:export")
    @PostMapping(value = "/system/config/${id}/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportAttachmentData(@PathVariable("id") Integer id) {
        super.locker(null, () -> configService.exportAttachmentData(id));
    }

    @Operation(summary = "导入附件")
    @SaCheckPermission("system:config:update")
    @PostMapping("/system/config/${id}/import")
    public Result<Boolean> importAttachmentData(@PathVariable("id") Integer id,
                                                @RequestParam("file") MultipartFile file) {
        configService.importAttachmentData(id, file);
        return Result.success(true);
    }


    @Operation(summary = "更新")
    @PostMapping("/config/update")
    @SaCheckPermission("support:config:update")
    public Result<Boolean> update(@RequestBody @Valid ConfigUpdateDTO updateDTO) {
        configService.update(updateDTO);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @PostMapping("/config/delete")
    @SaCheckPermission("support:config:delete")
    public Result<Boolean> delete(@RequestBody List<Integer> configIdList) {
        configService.delete(configIdList);
        return Result.success(true);
    }
}
