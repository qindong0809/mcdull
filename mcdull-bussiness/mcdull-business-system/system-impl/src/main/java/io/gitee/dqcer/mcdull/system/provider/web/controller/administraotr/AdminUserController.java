package io.gitee.dqcer.mcdull.system.provider.web.controller.administraotr;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.CaptchaVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICaptchaService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Tag(name = "Administrator")
public class AdminUserController extends BasicController {

    @Resource
    private IAdminUserService adminUserService;
    @Resource
    private IAdminMenuService adminMenuService;
    @Resource
    private ICaptchaService captchaService;

    @Operation(summary = "验证码")
    @GetMapping(GlobalConstant.ADMINISTRATOR_PATH + "/captcha/image")
    @SaIgnore
    public Result<Dict> getCaptcha() {
        String suffix = CharSequenceUtil.format("administrator:login:captcha:ip_addr:{}", IpUtil.getIpAddr(super.getRequest()));
        CaptchaVO captchaVO = super.rateLimiter(suffix, 5, 1, () -> captchaService.get());
        DateTime offset = DateUtil.offsetSecond(new Date(),  captchaVO.getExpireSeconds());
        Dict dict = Dict.create()
            .set("uuid", captchaVO.getCaptchaUuid())
            .set("isEnabled", true)
            .set("img", captchaVO.getCaptchaBase64Image())
            .set("expireTime", offset.getTime());
        return Result.success(dict);
    }

    //    @SaCheckEL("stpAdmin.checkPermission('system:area:export')")

    @Operation(summary = "Token")
    @PostMapping(GlobalConstant.ADMINISTRATOR_PATH +"/auth/login")
    public String adminAuth(@RequestBody @Valid LogonDTO dto) {
//        StpKit.ADMIN.checkPermission("system:administrator:authentication");
//        StpKit.ADMIN.checkLogin();
        String suffix = "administrator:login:" + dto.getLoginName();
        return super.rateLimiter(suffix, 1, 1, () -> adminUserService.auth(dto));
    }

    @Operation(summary = "获取用户信息", description = "获取登录用户信息")
    @GetMapping(GlobalConstant.ADMINISTRATOR_PATH +"/user/info")
    public Result<?> getUserInfo() {
//        return Result.success(adminUserService.getAdminInfo(UserContextHolder.userId()));
        return Result.success(adminMenuService.getMenuList());
    }
}
