package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.BizAuditVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IBizAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务审计控制器
 * @author dqcer
 * @since  2025/03/11
 */
@RestController
@Tag(name = "业务操作记录")
public class BizAuditController extends BasicController {

    @Resource
    private IBizAuditService bizAuditService;

    @Operation(summary = "分页查询")
    @PostMapping("/system/biz-audit/query")
    public Result<PagedVO<BizAuditVO>> queryPage(@RequestBody @Valid BizAuditQueryDTO dto) {
        return Result.success(bizAuditService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:biz_audit:export")
    @PostMapping(value = "/system/biz-audit/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid BizAuditQueryDTO dto) {
        super.locker(null, () -> bizAuditService.exportData(dto));
    }

    @Operation(summary = "分页查询")
    @PostMapping("/system/user-biz-audit/query")
    public Result<PagedVO<BizAuditVO>> queryPageByLoginUser(@RequestBody @Valid BizAuditQueryDTO dto) {
        dto.setOperator(UserContextHolder.getSession().getLoginName());
        return Result.success(bizAuditService.queryPage(dto));
    }
}
