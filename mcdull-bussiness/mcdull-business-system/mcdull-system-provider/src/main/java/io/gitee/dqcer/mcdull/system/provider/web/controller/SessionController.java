package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SessionQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.SessionVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 会话信息
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "会话信息")
@RequestMapping
public class SessionController extends BasicController {

    @Resource
    private ISessionService sessionService;

    @Operation(summary = "分页查询")
    @PostMapping("/session/queryPage")
    @SaCheckPermission("system:monitor:session_read")
    public Result<PagedVO<SessionVO>> queryPage(@RequestBody @Valid SessionQueryDTO dto) {
        return Result.success(sessionService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:session:export")
    @PostMapping(value = "/system/session/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid SessionQueryDTO dto) {
        super.locker(null, () -> sessionService.exportData(dto));
    }


    @Operation(summary = "批量强退")
    @GetMapping("/session/batchKickout")
    @SaCheckPermission("system:monitor:session_kickout")
    public Result<Boolean> batchDeleteMenu(@RequestParam("loginIdList") List<Integer> loginIdList) {
        sessionService.batchKickout(loginIdList);
        return Result.success(true);
    }



}
