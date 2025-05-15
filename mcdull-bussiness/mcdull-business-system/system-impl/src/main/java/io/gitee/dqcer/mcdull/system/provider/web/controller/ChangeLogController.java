package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ChangeLogAndVersionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IChangeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 更新日志
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "更新日志")
@RequestMapping
public class ChangeLogController extends BasicController {

    @Resource
    private IChangeLogService changeLogService;

    @Operation(summary = "添加")
    @PostMapping("/changeLog/add")
    @SaCheckPermission("support:changeLog:add")
    public Result<Boolean> add(@RequestBody @Valid ChangeLogAddDTO addForm) {
        final String key =  addForm.getVersion() + "_" + addForm.getType();
        return Result.success(super.locker(key, () -> changeLogService.add(addForm)));
    }

    @Operation(summary = "更新")
    @PostMapping("/changeLog/update")
    @SaCheckPermission("support:changeLog:update")
    public Result<Boolean> update(@RequestBody @Valid ChangeLogUpdateDTO updateForm) {
        changeLogService.update(updateForm);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/changeLog/batchDelete")
    @SaCheckPermission("support:changeLog:batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        changeLogService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "单个删除")
    @GetMapping("/changeLog/delete/{changeLogId}")
    @SaCheckPermission("support:changeLog:delete")
    public Result<Boolean> batchDelete(@PathVariable(value = "changeLogId") Integer changeLogId) {
        changeLogService.batchDelete(ListUtil.of(changeLogId));
        return Result.success(true);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/changeLog/queryPage")
    public Result<PagedVO<ChangeLogVO>> queryPage(@RequestBody @Valid ChangeLogQueryDTO queryForm) {
        return Result.success(changeLogService.queryPage(queryForm));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:changeLog:export")
    @PostMapping(value = "/system/changeLog/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid ChangeLogQueryDTO dto) {
        super.locker(null, () -> changeLogService.exportData(dto));
    }

    @Operation(summary = "查询全部和版本信息")
    @PostMapping("/changeLog-version")
    public Result<ChangeLogAndVersionVO> getChangeLogAndVersion() {
        return Result.success(changeLogService.getChangeLogAndVersion());
    }

    @Operation(summary = "变更内容详情")
    @GetMapping("/changeLog/getDetail/{changeLogId}")
    public Result<ChangeLogVO> getDetail(@PathVariable(value = "changeLogId") Integer changeLogId) {
        return Result.success(changeLogService.getById(changeLogId));
    }
}
