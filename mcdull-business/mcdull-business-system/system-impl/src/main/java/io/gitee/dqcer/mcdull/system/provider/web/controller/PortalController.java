package io.gitee.dqcer.mcdull.system.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.vo.PortalOverviewVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IPortalService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户聚合控制器
 *
 * @author dqcer
 */
@Tag(name = "门户")
@RestController
public class PortalController extends BasicController {

    @Resource
    private IPortalService portalService;
    @Resource
    private ITenantAuthService tenantAuthService;

    @Operation(summary = "首屏聚合数据")
    @GetMapping("/system/portal/overview")
    public Result<PortalOverviewVO> overview(HttpServletRequest request) {
        Integer tenantId = tenantAuthService.currentTenantId(request);
        if (tenantId != null) {
            tenantAuthService.validateCurrentTenant(tenantId);
        }
        return Result.success(portalService.overview(tenantId));
    }
}
