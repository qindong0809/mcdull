package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IChangeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "更新日志")
@RequestMapping
public class ChangeLogController {

    @Resource
    private IChangeLogService changeLogService;
    @Operation(summary = "添加")
    @PostMapping("/changeLog/add")
    @SaCheckPermission("support:changeLog:add")
    public Result<Boolean> add(@RequestBody @Valid ChangeLogAddDTO addForm) {
        changeLogService.add(addForm);
        return Result.success(true);
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
    public Result<Boolean> batchDelete(@PathVariable Integer changeLogId) {
        changeLogService.batchDelete(ListUtil.of(changeLogId));
        return Result.success(true);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/changeLog/queryPage")
    public Result<PagedVO<ChangeLogVO>> queryPage(@RequestBody @Valid ChangeLogQueryDTO queryForm) {
        return Result.success(changeLogService.queryPage(queryForm));
    }


    @Operation(summary = "变更内容详情")
    @GetMapping("/changeLog/getDetail/{changeLogId}")
    public Result<ChangeLogVO> getDetail(@PathVariable Integer changeLogId) {
        return Result.success(changeLogService.getById(changeLogId));
    }

}
