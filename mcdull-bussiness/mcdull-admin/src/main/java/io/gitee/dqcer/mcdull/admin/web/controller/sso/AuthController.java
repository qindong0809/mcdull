package io.gitee.dqcer.mcdull.admin.web.controller.sso;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.CurrentUserInfVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RouterVO;
import io.gitee.dqcer.mcdull.admin.web.service.sso.IAuthService;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 身份验证控制器
 *
 * @author dqcer
 * @since 2023/01/10 23:01:30
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
     * 得到当前用户信息
     *
     * @return {@link Result}<{@link CurrentUserInfVO}>
     */
    @GetMapping("getInfo")
    public Result<CurrentUserInfVO> getCurrentUserInfo() {
        return authService.getCurrentUserInfo();
    }

    @GetMapping("getRouters")
    public Result<List<RouterVO>> getCurrentUserRouters() {
        return authService.getCurrentUserRouters();
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @UnAuthorize
    @PostMapping("logout")
    public Result<String> logout() {
        return authService.logout();
    }
}
