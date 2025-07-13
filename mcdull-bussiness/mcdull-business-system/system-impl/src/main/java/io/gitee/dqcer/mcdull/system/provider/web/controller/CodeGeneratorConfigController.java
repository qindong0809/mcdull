package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorPreviewForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableConfigVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICodeGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 代码生成
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "代码生成")
@RequestMapping
public class CodeGeneratorConfigController extends BasicController {
    @Resource
    private ICodeGeneratorService codeGeneratorService;

    // ------------------- 查询 -------------------

    @Operation(summary = "获取表的列")
    @GetMapping("/support/codeGenerator/table/getTableColumns/{table}")
    @ResponseBody
    public Result<List<TableColumnVO>> getTableColumns(@PathVariable(value = "table") String table) {
        return Result.success(codeGeneratorService.getTableColumns(table));
    }

    @Operation(summary = "查询数据库表")
    @PostMapping("/support/codeGenerator/table/queryTableList")
    @ResponseBody
    @SaCheckPermission("support:code_generator:read")
    public Result<PagedVO<TableVO>> queryTableList(@RequestBody @Valid TableQueryForm tableQueryForm) {
        return Result.success(codeGeneratorService.queryTableList(tableQueryForm));
    }

    // ------------------- 配置 -------------------

    @Operation(summary = "获取配置")
    @GetMapping("/support/codeGenerator/table/getConfig/{table}")
    @ResponseBody
    public Result<TableConfigVO> getTableConfig(@PathVariable(value = "table") String table) {
        return Result.success(codeGeneratorService.getTableConfig(table));
    }

    @Operation(summary = "更新配置")
    @PostMapping("/support/codeGenerator/table/updateConfig")
    @ResponseBody
    @SaCheckPermission("support:code_generator:write")
    public Result<Boolean> updateConfig(@RequestBody @Valid CodeGeneratorConfigForm form) {
        codeGeneratorService.updateConfig(form);
        return Result.success(true);
    }

    // ------------------- 生成 -------------------

    @Operation(summary = "预览")
    @PostMapping("/support/codeGenerator/code/preview")
    @ResponseBody
    @SaCheckPermission("support:code_generator:write")
    public Result<String> preview(@RequestBody @Valid CodeGeneratorPreviewForm form) {
        return Result.success(codeGeneratorService.preview(form));
    }

    @Operation(summary = "下载")
    @SaCheckPermission("support:code_generator:write")
    @GetMapping(value = "/support/codeGenerator/code/download/{tableName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(@PathVariable(value = "tableName") String tableName, HttpServletResponse response) {
        super.locker(tableName, () -> {
            byte[] dataStream = codeGeneratorService.download(tableName);
            ServletUtil.download(tableName + "_code.zip", dataStream);
            return true;
        });
    }

}
