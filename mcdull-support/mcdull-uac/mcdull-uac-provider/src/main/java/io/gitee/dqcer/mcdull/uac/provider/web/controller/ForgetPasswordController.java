package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRequestDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRestDTO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IForgetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 登录控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RestController
@Tag(name = "忘记密码")
@RequestMapping("/forget-password")
public class ForgetPasswordController {

    @Resource
    private IForgetPasswordService forgetPasswordService;

    @SaIgnore
    @Operation(summary = "申请")
    @PostMapping("request")
    public Result<Boolean> request(@RequestBody @Valid ForgetPasswordRequestDTO dto) {
        forgetPasswordService.request(dto);
        return Result.success(true);
    }

    @SaIgnore
    @Operation(summary = "重置")
    @PostMapping("update")
    public Result<Boolean> reset(@RequestBody @Valid ForgetPasswordRestDTO dto) {
        forgetPasswordService.reset(dto);
        return Result.success(true);
    }

}
