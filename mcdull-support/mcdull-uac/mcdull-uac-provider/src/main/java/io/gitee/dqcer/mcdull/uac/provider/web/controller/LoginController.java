package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.client.api.AuthServiceApi;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "认证授权")
@RequestMapping("sso")
public class LoginController implements AuthServiceApi {

    @Resource
    private LoginService loginService;

    /**
     * 登录 21232F297A57A5A743894A0E4A801FC3
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link LoginVO}>
     */
    @Operation(summary = "登录", description = "login api")
    @PostMapping("login")
    public Result<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        loginService.login(loginDTO.getUsername(), loginDTO.getPassword(), loginDTO.getCode(),
                loginDTO.getUuid());
        return Result.success();
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @PostMapping("logout")
    @Operation(summary = "注销", description = "logout api")
    public Result<String> logout() {
        loginService.logout();
        return Result.success();
    }

    /**
     * 验证token
     *
     * @param token token
     * @return {@link Long}
     */
    @UnAuthorize
    @Override
    public Result<Long> tokenValid(String token, String traceId) {
        StpUtil.checkLogin();
        return Result.success(StpUtil.getLoginIdAsLong());
    }

    @Override
    public Result<List<String>> getPermissionList(Long userId) {
        return loginService.getPermissionList(userId);
    }

    @Override
    public Result<List<String>> getRoleList(Long userId) {
        return loginService.getRoleList(userId);
    }
}
