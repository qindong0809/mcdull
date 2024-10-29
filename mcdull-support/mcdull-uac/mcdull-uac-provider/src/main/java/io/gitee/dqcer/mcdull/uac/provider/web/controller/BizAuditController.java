package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.BizAuditVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IBizAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Tag(name = "业务操作记录")
public class BizAuditController extends BasicController {

    @Resource
    private IBizAuditService bizAuditService;

    @Operation(summary = "分页查询")
    @PostMapping("/biz-audit/queryPage")
    public Result<PagedVO<BizAuditVO>> queryPage(@RequestBody @Valid BizAuditQueryDTO queryForm) {
        return Result.success(bizAuditService.queryPage(queryForm));
    }
}
