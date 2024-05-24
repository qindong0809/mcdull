package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SessionQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SessionVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISessionService;
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
@Tag(name = "会话信息")
@RequestMapping
public class SessionController {

    @Resource
    private ISessionService sessionService;

    @Operation(summary = "分页查询")
    @PostMapping("/session/queryPage")
    @SaCheckPermission("system:monitor:session_read")
    public Result<PagedVO<SessionVO>> queryPage(@RequestBody @Valid SessionQueryDTO dto) {
        return Result.success(sessionService.queryPage(dto));
    }

    @Operation(summary = "批量强退")
    @GetMapping("/session/batchKickout")
    @SaCheckPermission("system:monitor:session_kickout")
    public Result<Boolean> batchDeleteMenu(@RequestParam("loginIdList") List<Long> loginIdList) {
        sessionService.batchKickout(loginIdList);
        return Result.success(true);
    }



}
