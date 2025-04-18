package io.gitee.dqcer.mcdull.system.provider.web.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.web.service.IServerMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.Map;

@RestController
@Tag(name = "服务监控")
@RequestMapping
public class ServerMonitorController {

    @Resource
    private IServerMonitorService serviceMonitorService;

    @Operation(summary = "查询服务监控")
    @GetMapping("/monitor/server")
    @SaCheckPermission("system:monitor_server:read")
    public Result<Map<String, Object>> queryMonitor(){
        return Result.success(serviceMonitorService.getServers());
    }
}
