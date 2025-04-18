package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.temp.SaTempUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ForgetPasswordRequestDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ForgetPasswordRestDTO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IForgetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 登录控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RestController
@Tag(name = "忘记密码")
@RequestMapping("/forget-password")
public class ForgetPasswordController extends BasicController {

    @Resource
    private IForgetPasswordService forgetPasswordService;

    @SaIgnore
    @Operation(summary = "申请")
    @PostMapping("request")
    public Result<Boolean> request(@RequestBody @Valid ForgetPasswordRequestDTO dto) {
        String key = StrUtil.format("forget:password:request:user_identity:{}", dto.getUserIdentity());
        super.rateLimiter(key, 5, 1, () -> forgetPasswordService.request(dto));
        return Result.success(true);
    }

    @SaIgnore
    @Operation(summary = "重置")
    @PostMapping("update")
    public Result<Boolean> reset(@RequestBody @Valid ForgetPasswordRestDTO dto) {
        String key = StrUtil.format("forget:password:update:token:{}", dto.getToken());
        super.rateLimiter(key, 5, 1, o -> {
            String token = dto.getToken();
            Integer userId = SaTempUtil.parseToken(token, Integer.class);
            if (ObjectUtil.isNull(userId)) {
                // 链接无效
                throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
            }
            forgetPasswordService.reset(userId, dto);
        });
        return Result.success(true);
    }
}
