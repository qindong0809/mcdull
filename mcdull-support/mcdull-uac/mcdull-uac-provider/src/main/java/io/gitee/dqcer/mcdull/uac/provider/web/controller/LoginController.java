package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.client.api.AuthServiceApi;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.CaptchaVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PermissionRouterVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 登录控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RestController
@Tag(name = "登录认证")
public class LoginController implements AuthServiceApi {

    @Resource
    private ILoginService loginService;
    @Resource
    private ICaptchaService captchaService;

    @Operation(summary = "验证码")
    @GetMapping("/login/getCaptcha")
    @SaIgnore
    public Result<CaptchaVO> getCaptcha() {
        return Result.success(captchaService.get());
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<LogonVO> login(@RequestBody @Valid LoginDTO dto) {
        return Result.success(loginService.login(dto));
    }

    @GetMapping("/current/user-info")
    @Operation(summary = "登录人信息", description = "角色、权限、个人信息")
    public Result<LogonVO> getCurrentUserInfo() {
        return Result.success(loginService.getCurrentUserInfo());
    }

    @Operation(summary = "注销")
    @GetMapping("/login/logout")
    public Result<Boolean> logout() {
        loginService.logout();
        return Result.success(true);
    }


    @UnAuthorize
    @Override
    public Result<Integer> tokenValid(String token, String traceId) {
        StpUtil.checkLogin();
        return Result.success(StpUtil.getLoginIdAsInt());
    }

    @Override
    public Result<List<String>> getPermissionList(Long userId) {
        return Result.success(loginService.getPermissionList(userId));
    }

    @Override
    public Result<List<String>> getRoleList(Long userId) {
        return Result.success(loginService.getRoleList(userId));
    }
}
