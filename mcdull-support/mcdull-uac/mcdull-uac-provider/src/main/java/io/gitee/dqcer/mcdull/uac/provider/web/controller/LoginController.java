package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.client.api.AuthServiceApi;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginUserVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RouterVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
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
@Tag(name = "认证授权")
@RequestMapping("sso")
public class LoginController implements AuthServiceApi {

    @Resource
    private ILoginService loginService;

    @Resource
    private IUserService userService;

    @Resource
    private IMenuService menuService;

    /**
     * 登录 21232F297A57A5A743894A0E4A801FC3
     *
     * @param dto 登录dto
     * @return {@link Result}<{@link LoginUserVO}>
     */
    @Operation(summary = "登录", description = "Default username=admin  password=21232F297A57A5A743894A0E4A801FC3")
    @PostMapping("login")
    public Result<String> login(@RequestBody @Valid LoginDTO dto) {
        loginService.login(dto.getUsername(), dto.getPassword(), dto.getCode(), dto.getUuid());
        return Result.success(StpUtil.getTokenValue());
    }

    @Operation(summary = "当前登录人信息", description = "角色、权限、个人信息")
    @GetMapping("getInfo")
    public Result<LoginUserVO> getInfo() {
        LoginUserVO vo = new LoginUserVO();
        Long currentUserId = UserContextHolder.currentUserId();
        vo.setUser(userService.get(currentUserId));
        vo.setRoleCodeList(loginService.getRoleList(currentUserId));
        vo.setPermissionCodeList(loginService.getPermissionList(currentUserId));
        return Result.success(vo);
    }

    @GetMapping("getRouters")
    public Result<RouterVO> getRouters() {
        Long userId = UserContextHolder.currentUserId();
        RouterVO routerVO = menuService.tree(userId);
        return Result.success(routerVO);
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @PostMapping("logout")
    @Operation(summary = "注销当前token", description = "logout api")
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
        return Result.success(loginService.getPermissionList(userId));
    }

    @Override
    public Result<List<String>> getRoleList(Long userId) {
        return Result.success(loginService.getRoleList(userId));
    }
}
