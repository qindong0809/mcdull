package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.flow.ProcessFlow;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.uac.provider.flow.login.LoginContext;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.CaptchaVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICaptchaService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Tag(name = "登录认证")
public class LoginController extends BasicController {

    @Resource
    private ILoginService loginService;
    @Resource
    private ICaptchaService captchaService;
    @Resource
    private ProcessFlow processFlow;

    @Operation(summary = "验证码")
    @GetMapping("/login/getCaptcha")
    @SaIgnore
    public Result<CaptchaVO> getCaptcha() {
        String suffix = StrUtil.format("login:captcha:ip_addr:{}", IpUtil.getIpAddr(super.getRequest()));
        return Result.success(
                super.rateLimiter(suffix, 5, 1, () -> captchaService.get()));
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<LogonVO> login(@RequestBody @Valid LoginDTO dto) throws Exception {
        return Result.success(
                super.rateLimiter("login:", 5, 1, () -> {
                    LoginContext context = new LoginContext();
                    context.setId("process_flow_login");
                    context.setLoginDTO(dto);
                    processFlow.run(context);
                    return context.getVo();
                }));

    }

    /**
     * 与login返回一致，待确认是否重复
     * @return
     */
    @Deprecated
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

}
