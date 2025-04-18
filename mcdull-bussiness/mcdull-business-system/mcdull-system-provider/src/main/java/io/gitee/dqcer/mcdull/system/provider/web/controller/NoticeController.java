package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeDetailVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.INoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2024-05-18 19:47:13
 */
@RestController
@Tag(name = "通知")
public class NoticeController extends BasicController {

    @Resource
    private INoticeService noticeService;

    @Operation(summary = "分页")
    @PostMapping("/notice/query")
    public Result<PagedVO<NoticeVO>> queryPage(@RequestBody @Valid NoticeQueryDTO dto) {
        return Result.success(noticeService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:notice:export")
    @PostMapping(value = "/system/notice/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid NoticeQueryDTO dto) {
        super.locker(null, () -> noticeService.exportData(dto));
    }

    @Operation(summary = "添加")
    @PostMapping("/notice/add")
    @SaCheckPermission("system:notice:write")
    public Result<Boolean> add(@RequestBody @Valid NoticeAddDTO dto) {
        noticeService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新详情")
    @GetMapping("/notice/getUpdateVO/{noticeId}")
    @SaCheckPermission("system:notice:write")
    public Result<NoticeUpdateFormVO> getUpdateFormVO(@PathVariable(value = "noticeId") Integer noticeId) {
        return Result.success(noticeService.getUpdateFormVO(noticeId));
    }

    @Operation(summary = "更新")
    @SaCheckPermission("system:notice:write")
    @PostMapping("/notice/update")
    public Result<Boolean> update(@RequestBody @Valid NoticeUpdateDTO dto) {
        noticeService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @SaCheckPermission("system:notice:write")
    @PostMapping("/notice/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        noticeService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @SaCheckPermission("system:notice:write")
    @GetMapping("/notice/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        noticeService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }

    @Operation(summary = "【员工】通知公告-查看详情")
    @GetMapping("/notice/employee/view/{noticeId}")
    public Result<NoticeDetailVO> view(@PathVariable(value = "noticeId") Integer noticeId) {
        return Result.success(noticeService.view(noticeId));
    }

    @Operation(summary = "【员工】通知公告-查询全部/未读")
    @PostMapping("notice/employee/query")
    public Result<PagedVO<NoticeUserVO>> queryUserNotice(@RequestBody @Valid NoticeEmployeeQueryDTO dto) {
        return Result.success(noticeService.queryUserNotice(dto));
    }

}
