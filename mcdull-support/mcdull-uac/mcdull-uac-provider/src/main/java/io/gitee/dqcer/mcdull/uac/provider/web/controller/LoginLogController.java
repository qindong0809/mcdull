package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IChangeLogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLogService;
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
}
