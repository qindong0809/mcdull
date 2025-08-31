package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.NameValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.OperateLogVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
* 请求监控
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "请求监控")
@RequestMapping
public class OperateLogController extends BasicController {

    @Resource
    private IOperateLogService operateLogService;

    @Operation(summary = "home")
    @PostMapping("/operateLog/home")
    public Result<KeyValueVO<List<String>, List<Integer>>> home() {
        return Result.success(operateLogService.homePie());
    }

    @Operation(summary = "pie-home")
    @PostMapping("/operateLog/pie-home")
    public Result<List<NameValueVO<String, Integer>>> pieHome() {
        return Result.success(operateLogService.pieHome());
    }


    @Operation(summary = "分页查询")
    @PostMapping("/support/operateLog/page/query")
    @SaCheckEL("stp.checkPermission('support:operateLog:query')")
    public Result<PagedVO<OperateLogVO>> queryByPage(@RequestBody OperateLogQueryDTO dto) {
        return Result.success(operateLogService.queryByPage(dto));
    }

    @Operation(summary = "导出数据")
    @PostMapping(value ="/system/operateLog/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @SaCheckEL("stp.checkPermission('system:operateLog:export')")
    public void exportData(@RequestBody OperateLogQueryDTO dto) {
        super.locker(null, () -> operateLogService.exportData(dto));
    }

    @Operation(summary = "详情")
    @GetMapping("/support/operateLog/detail/{operateLogId}")
    @SaCheckEL("stp.checkPermission('support:operateLog:detail')")
    public Result<OperateLogVO> detail(@PathVariable(value = "operateLogId") Integer operateLogId) {
        return Result.success(operateLogService.detail(operateLogId));
    }

    @Operation(summary = "分页查询当前登录人信息")
    @PostMapping("/operateLog/page/query/login")
    public Result<PagedVO<OperateLogVO>> queryByPageLogin(@RequestBody OperateLogQueryDTO dto) {
        dto.setUserId(UserContextHolder.userId());
        return Result.success(operateLogService.queryByPage(dto));
    }

}
