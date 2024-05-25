package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.OperateLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "请求监控")
@RequestMapping
public class OperateLogController {

    @Resource
    private IOperateLogService operateLogService;


    @Operation(summary = "分页查询")
    @PostMapping("/operateLog/page/query")
    @SaCheckPermission("support:operateLog:query")
    public Result<PagedVO<OperateLogVO>> queryByPage(@RequestBody OperateLogQueryDTO dto) {
        return Result.success(operateLogService.queryByPage(dto));
    }

    @Operation(summary = "详情")
    @GetMapping("/operateLog/detail/{operateLogId}")
    @SaCheckPermission("support:operateLog:detail")
    public Result<OperateLogVO> detail(@PathVariable Integer operateLogId) {
        return Result.success(operateLogService.detail(operateLogId));
    }

}
