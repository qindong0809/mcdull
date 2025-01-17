package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "登录日志")
@RequestMapping
public class LoginLogController {

    @Resource
    private ILoginLogService loginLogService;


    @Operation(summary = "分页查询")
    @PostMapping("/loginLog/page/query")
    @SaCheckPermission("support:loginLog:query")
    public Result<PagedVO<LoginLogVO>> queryByPage(@RequestBody LoginLogQueryDTO queryForm) {

        return Result.success(loginLogService.queryByPage(queryForm));
    }

    @Operation(summary = "分页查询当前登录人信息")
    @PostMapping("/loginLog/page/query/login")
    public Result<PagedVO<LoginLogVO>> queryByPageLogin(@RequestBody LoginLogQueryDTO queryForm) {
        queryForm.setUserId(UserContextHolder.userId());
        return Result.success(loginLogService.queryByPage(queryForm));
    }
}
