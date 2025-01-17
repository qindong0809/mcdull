package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.PasswordPolicyDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IPasswordPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 密码策略控制器
 *
 * @author dqcer
 * @since 2024/06/03
 */
@RestController
@Tag(name = "密码策略")
@RequestMapping
public class PasswordPolicyController {

    @Resource
    private IPasswordPolicyService passwordPolicyService;

    @Operation(summary = "详情")
    @GetMapping("password-policy/detail")
    @SaCheckPermission("support:password_policy:read")
    public Result<PasswordPolicyVO> detail() {
        return Result.success(passwordPolicyService.detail());
    }

    @Operation(summary = "更新")
    @PostMapping("password-policy/update")
    @SaCheckPermission("support:password_policy:write")
    public Result<Boolean> update(@RequestBody @Valid PasswordPolicyDTO dto) {
        passwordPolicyService.update(dto);
        return Result.success(true);
    }
}
