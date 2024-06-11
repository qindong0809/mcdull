package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRequestDTO;
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

    @Operation(summary = "申请")
    @PostMapping("request")
    public Result<String> request(@RequestBody @Valid ForgetPasswordRequestDTO dto) {
        return Result.success(forgetPasswordService.request(dto));
    }

    @Operation(summary = "重置")
    @PostMapping("update")
    public Result<String> reset(@RequestBody @Valid ForgetPasswordRequestDTO dto) {
        return Result.success(forgetPasswordService.request(dto));
    }

}
