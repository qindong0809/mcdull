package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @SaCheckEL("stp.checkPermission('support:form:page')")
    @PostMapping("/form/queryPage")
    public Result<PagedVO<FormVO>> queryPage(@RequestBody @Valid FormQueryDTO dto) {
        return Result.success(formService.queryPage(dto));
    }

    @Operation(summary = "新增")
    @SaCheckEL("stp.checkPermission('support:form:add')")
    @PostMapping("/form/add")
    public Result<Boolean> add(@RequestBody @Valid FormAddDTO dto) {
        formService.add(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新基本信息")
    @SaCheckEL("stp.checkPermission('support:form:update')")
    @PostMapping("/form/update")
    public Result<Boolean> update(@RequestBody @Valid FormUpdateDTO dto) {
        formService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @SaCheckEL("stp.checkPermission('support:form:delete')")
    @GetMapping("/form/delete/{formId}")
    public Result<Boolean> delete(@PathVariable(value = "formId") Integer formId) {
        formService.delete(formId);
        return Result.success(true);
    }

    @Operation(summary = "表单设计")
    @SaCheckEL("stp.checkPermission('support:form:designer')")
    @PostMapping("/form/update-form")
    public Result<Boolean> updateJsonText(@RequestBody @Valid FormUpdateJsonTextDTO dto) {
        formService.updateJsonText(dto);
        return Result.success(true);
    }

    @Operation(summary = "表单发布")
    @SaCheckEL("stp.checkPermission('support:form:publish')")
    @PostMapping("/form/config-ready")
    public Result<Boolean> formConfigReady(@RequestBody @Valid FormConfigReadyDTO dto) {
        formService.formConfigReady(dto.getFormId());
        return Result.success(true);
    }

    @Operation(summary = "表单JSON详情")
    @GetMapping("/form/detail/{formId}")
    public Result<FormVO> detail(@PathVariable(value = "formId") Integer formId) {
        return Result.success(formService.detail(formId));
    }

    @Operation(summary = "表单字段详情")
    @GetMapping("/form/item-config-list/{formId}")
    public Result<List<FormItemVO>> itemConfigList(@PathVariable(value = "formId") Integer formId) {
        return Result.success(formService.itemConfigList(formId));
    }

    @Operation(summary = "添加form数据")
    @SaCheckEL("stp.checkPermission('support:form:record:add')")
    @PostMapping("/form/record-add")
    public Result<Boolean> recordAdd(@RequestBody @Valid FormRecordAddDTO dto) {
        formService.recordAdd(dto);
        return Result.success(true);
    }

    @Operation(summary = "获取form数据信息")
    @SaCheckEL("stp.checkPermission('support:form:record:list')")
    @PostMapping("/form/record-queryPage")
    public Result<PagedVO<Map<String, String>>> recordQueryPage(@RequestBody @Valid FormRecordQueryDTO dto) {
        return Result.success(formService.recordQueryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckEL("stp.checkPermission('support:form:record:export')")
    @PostMapping(value = "/form/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid FormRecordQueryDTO dto) {
       formService.exportData(dto);
    }

    @Operation(summary = "删除单条数据")
    @SaCheckEL("stp.checkPermission('support:form:record:delete')")
    @GetMapping("/form/record-delete/{recordId}")
    public Result<Boolean> deleteOneRecord(@PathVariable(value = "recordId") Integer recordId) {
        formService.deleteOneRecord(recordId);
        return Result.success(true);
    }

    @Operation(summary = "更新单条数据")
    @SaCheckEL("stp.checkPermission('support:form:record:update')")
    @PostMapping("/form/record-update")
    public Result<Boolean> updateOneRecord(@RequestBody @Valid FormRecordUpdateDTO dto) {
        formService.updateOneRecord(dto);
        return Result.success(true);
    }

    @Operation(summary = "获取单条记录不用转")
    @GetMapping("/form/record-detail-no-convert/{recordId}")
    public Result<Map<String, Object>> getOneRecordNoConvert(@PathVariable(value = "recordId") Integer recordId) {
        return Result.success(formService.getOneRecordNoConvert(recordId));
    }

}
