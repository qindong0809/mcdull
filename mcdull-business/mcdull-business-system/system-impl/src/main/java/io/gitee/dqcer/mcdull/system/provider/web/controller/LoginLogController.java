package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "登录日志")
@RequestMapping
public class LoginLogController extends BasicController {

    @Resource
    private ILoginLogService loginLogService;


    @Operation(summary = "分页查询")
    @PostMapping("/loginLog/page/query")
    @SaCheckEL("stp.checkPermission('support:loginLog:query')")
    public Result<PagedVO<LoginLogVO>> queryByPage(@RequestBody LoginLogQueryDTO queryForm) {
        return Result.success(loginLogService.queryByPage(queryForm));
    }

    @Operation(summary = "导出数据")
    @SaCheckEL("stp.checkPermission('system:loginLog:export')")
    @PostMapping(value = "/system/loginLog/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid LoginLogQueryDTO dto) {
        super.locker(null, () -> loginLogService.exportData(dto));
    }

    @Operation(summary = "分页查询当前登录人信息")
    @PostMapping("/loginLog/page/query/login")
    public Result<PagedVO<LoginLogVO>> queryByPageLogin(@RequestBody LoginLogQueryDTO queryForm) {
        queryForm.setUserId(UserContextHolder.userId());
        return Result.success(loginLogService.queryByPage(queryForm));
    }
}
