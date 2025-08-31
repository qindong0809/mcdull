package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EmailConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EmailConfigVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISysInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * @author dqcer
 * @since 2024/06/03
 */
@RestController
@Tag(name = "邮箱配置")
@RequestMapping
public class EmailConfigController {

    @Resource
    private ISysInfoService sysInfoService;

    @Operation(summary = "详情")
    @GetMapping("email-config/detail")
    @SaCheckEL("stp.checkPermission('support:email_config:read')")
    public Result<EmailConfigVO> detail() {
        return Result.success(sysInfoService.detail());
    }

    @Operation(summary = "更新")
    @PostMapping("email-config/update")
    @SaCheckEL("stp.checkPermission('support:email_config:write')")
    public Result<Boolean> update(@RequestBody @Valid EmailConfigDTO dto) {
        sysInfoService.update(dto);
        return Result.success(true);
    }
}
