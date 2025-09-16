package io.gitee.dqcer.mcdull.system.provider.web.controller.administraotr;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Administrator")
public class AdminUserController extends BasicController {

    @Resource
    private IAdminUserService adminUserService;

    //    @SaCheckEL("stpAdmin.checkPermission('system:area:export')")

    @Operation(summary = "Token")
    @PostMapping("/administrator/${mcdull.administrator.authentication.path:auth}")
    public String adminAuth(@RequestBody @Valid LogonDTO dto) {
        StpKit.ADMIN.checkPermission("system:administrator:authentication");
        StpKit.ADMIN.checkLogin();
        String suffix = "administrator:login:" + dto.getLoginName();
        return super.rateLimiter(suffix, 1, 1, () -> adminUserService.auth(dto));
    }
}
