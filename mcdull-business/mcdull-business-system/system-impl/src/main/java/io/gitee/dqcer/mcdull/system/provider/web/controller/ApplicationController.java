package io.gitee.dqcer.mcdull.system.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ApplicationVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用控制器
 *
 * @author dqcer
 * @since 2026/11/23
 */
@Tag(name = "应用中心")
@RestController
public class ApplicationController extends BasicController {

    @Resource
    private IApplicationService applicationService;

    @Operation(summary = "当前用户可见应用列表")
    @GetMapping("/application/list")
    public Result<List<ApplicationVO>> list() {
        return Result.success(applicationService.listVisible());
    }
}
