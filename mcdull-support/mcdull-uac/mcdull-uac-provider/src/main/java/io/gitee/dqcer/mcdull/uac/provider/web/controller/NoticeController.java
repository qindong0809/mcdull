package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeUpdateDTO;
import java.util.List;

import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeDetailVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUserVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author dqcer
 * @since 2024-05-18 19:47:13
 */
@RestController
@Tag(name = "通知")
public class NoticeController {

    @Resource
    private INoticeService noticeService;

    @Operation(summary = "分页")
    @PostMapping("/notice/query")
    public Result<PagedVO<NoticeVO>> queryPage(@RequestBody @Valid NoticeQueryDTO dto) {
        return Result.success(noticeService.queryPage(dto));
    }

    @Operation(summary = "添加")
    @PostMapping("/notice/add")
    public Result<Boolean> add(@RequestBody @Valid NoticeAddDTO dto) {
        noticeService.insert(dto);
        return Result.success(true);
    }


    @Operation(summary = "更新详情")
    @GetMapping("/notice/getUpdateVO/{noticeId}")
    @SaCheckPermission("oa:notice:update")
    public Result<NoticeUpdateFormVO> getUpdateFormVO(@PathVariable Integer noticeId) {
        return Result.success(noticeService.getUpdateFormVO(noticeId));
    }

    @Operation(summary = "更新")
    @SaCheckPermission("oa:notice:update")
    @PostMapping("/notice/update")
    public Result<Boolean> update(@RequestBody @Valid NoticeUpdateDTO dto) {
        noticeService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/notice/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        noticeService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/notice/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable Integer id) {
        noticeService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }

    @Operation(summary = "【员工】通知公告-查看详情")
    @GetMapping("/notice/employee/view/{noticeId}")
    public Result<NoticeDetailVO> view(@PathVariable Integer noticeId) {
        return Result.success(noticeService.view(noticeId));
    }

    @Operation(summary = "【员工】通知公告-查询全部/未读")
    @PostMapping("notice/employee/query")
    public Result<PagedVO<NoticeUserVO>> queryUserNotice(@RequestBody @Valid NoticeEmployeeQueryDTO dto) {
        return Result.success(noticeService.queryUserNotice(dto));
    }

}
