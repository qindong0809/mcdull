package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Administrator")
public class AdministratorController extends BasicController {

    @SaIgnore
    @Operation(summary = "Token")
    @PostMapping("/administrator/${mcdull.administrator.authentication.path:auth}")
    public String token() {

        return "administrator-token";
    }
}
