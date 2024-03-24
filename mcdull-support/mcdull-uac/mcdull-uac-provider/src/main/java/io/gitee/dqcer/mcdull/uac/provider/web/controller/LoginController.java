package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.client.api.AuthServiceApi;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PermissionRouterVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 登录控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RestController
@Tag(name = "认证授权")
public class LoginController implements AuthServiceApi {

    @Resource
    private ILoginService loginService;

    @Resource
    private IUserService userService;

    @Resource
    private IMenuService menuService;

    @Resource
    private IRoleService roleService;

    /**
     * 登录 21232F297A57A5A743894A0E4A801FC3
     *
     * @param dto 登录dto
     * @return {@link Result}
     */
    @Operation(summary = "登录", description = "Default username=admin  password=21232F297A57A5A743894A0E4A801FC3")
    @PostMapping("login")
    public Result<LogonVO> login(@RequestBody @Valid LoginDTO dto) {
        loginService.login(dto.getUsername(), dto.getPassword(), dto.getCode(), dto.getUuid());
        Integer userId = StpUtil.getLoginId(0);
        LogonVO vo = new LogonVO();
        vo.setAccessToken(StpUtil.getTokenValue());
        vo.setRefreshToken(StpUtil.getTokenValue());
        vo.setExpires(DateUtil.offsetDay(new Date(), 1));
        UserVO user = userService.get(userId);
        if (ObjUtil.isNotNull(user)) {
            vo.setUsername(user.getNickname());
        }
        vo.setRoles(loginService.getRoleList(userId));
        return Result.success(vo);
    }

    @Operation(summary = "当前登录人信息", description = "角色、权限、个人信息")
    @GetMapping("getInfo")
    public JSONObject getInfo() {
        Integer currentUserId = UserContextHolder.currentUserId();
        Result<UserVO> success = Result.success();
        JSONObject jsonObject = JSONUtil.parseObj(success);
        jsonObject.set("permissions", loginService.getPermissionList(currentUserId));
        jsonObject.set("roles", loginService.getRoleList(currentUserId));
        jsonObject.set("user", userService.get(currentUserId));
        return jsonObject;
    }

    @Operation(summary = "当前登录人角色信息", description = "角色")
    @GetMapping("role-list")
    public Result<List<LabelValueVO<Integer, String>>> getRoleList() {
        Integer currentUserId = UserContextHolder.currentUserId();
        return Result.success(roleService.getSimple(currentUserId));
    }

    @GetMapping("getRouters/{roleId}")
    public Result<List<PermissionRouterVO>> getPermissionRouter(@PathVariable("roleId") Integer roleId) {
        Integer userId = UserContextHolder.currentUserId();
        UserVO userVO = userService.get(userId);
        if (ObjUtil.isNull(userVO)) {
            return Result.success();
        }
        if (GlobalConstant.SUPER_ADMIN_USER_TYPE.equals(userVO.getType())) {
            return Result.success(menuService.getPermissionRouter());
        }
        List<PermissionRouterVO> routerVO = menuService.getPermissionRouterByRole(roleId);
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
    public Result<Integer> tokenValid(String token, String traceId) {
        StpUtil.checkLogin();
        return Result.success(StpUtil.getLoginIdAsInt());
    }

    @Override
    public Result<List<String>> getPermissionList(Integer userId) {
        return Result.success(loginService.getPermissionList(userId));
    }

    @Override
    public Result<List<String>> getRoleList(Integer userId) {
        return Result.success(loginService.getRoleList(userId));
    }
}
