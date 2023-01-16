package com.dqcer.mcdull.admin.web.controller.sso;

import com.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import com.dqcer.mcdull.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.model.dto.sys.LoginDTO;
import com.dqcer.mcdull.admin.web.service.sso.IAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 身份验证控制器
 *
 * @author dqcer
 * @date 2023/01/10 23:01:30
 */
@RestController
@RequestMapping("sso")
public class AuthController {

    @Resource
    private IAuthService authService;

    /**
     * 登录 21232F297A57A5A743894A0E4A801FC3
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link String}>
     */
    @UnAuthorize
    @PostMapping("login")
    public Result<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @PostMapping("logout")
    public Result<String> logout() {
        return authService.logout();
    }
}
