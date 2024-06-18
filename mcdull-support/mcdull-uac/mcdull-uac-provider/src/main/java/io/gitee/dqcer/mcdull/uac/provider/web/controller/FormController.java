package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@RestController
@Tag(name = "动态表单")
public class FormController {

    @Resource
    private IFormService formService;

    @Operation(summary = "分页")
//    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/queryPage")
    public Result<PagedVO<FormVO>> queryPage(@RequestBody @Valid FormQueryDTO dto) {
        return Result.success(formService.queryPage(dto));
    }

    @Operation(summary = "新增")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/add")
    public Result<Boolean> add(@RequestBody @Valid FormAddDTO dto) {
        formService.add(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新基本信息")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/update")
    public Result<Boolean> update(@RequestBody @Valid FormUpdateDTO dto) {
        formService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    //    @SaCheckPermission("support:changeLog:add")
    @GetMapping("/form/delete/{formId}")
    public Result<Boolean> delete(@PathVariable Integer formId) {
        formService.delete(formId);
        return Result.success(true);
    }

    @Operation(summary = "更新form信息")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/update-form")
    public Result<Boolean> updateJsonText(@RequestBody @Valid FormUpdateJsonTextDTO dto) {
        formService.updateJsonText(dto);
        return Result.success(true);
    }

    @Operation(summary = "form发布")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/config-ready")
    public Result<Boolean> formConfigReady(@RequestBody @Valid FormConfigReadyDTO dto) {
        formService.formConfigReady(dto.getFormId());
        return Result.success(true);
    }

    @Operation(summary = "表单JSON详情")
    //    @SaCheckPermission("support:changeLog:add")
    @GetMapping("/form/detail/{formId}")
    public Result<FormVO> detail(@PathVariable Integer formId) {
        return Result.success(formService.detail(formId));
    }

    @Operation(summary = "表单字段详情")
    //    @SaCheckPermission("support:changeLog:add")
    @GetMapping("/form/item-config-list/{formId}")
    public Result<List<FormItemVO>> itemConfigList(@PathVariable Integer formId) {
        return Result.success(formService.itemConfigList(formId));
    }

    @Operation(summary = "添加form数据信息")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/record-add")
    public Result<Boolean> recordAdd(@RequestBody @Valid FormRecordAddDTO dto) {
        formService.recordAdd(dto);
        return Result.success(true);
    }

    @Operation(summary = "获取form数据信息")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/record-queryPage")
    public Result<PagedVO<FormRecordDataVO>> recordQueryPage(@RequestBody @Valid FormRecordQueryDTO dto) {
        return Result.success(formService.recordQueryPage(dto));
    }

    @Operation(summary = "获取form数据信息")
    //    @SaCheckPermission("support:changeLog:add")
    @PostMapping("/form/record-export")
    public void exportData(@RequestBody @Valid FormRecordQueryDTO dto) {
        formService.exportData(dto);
    }

}