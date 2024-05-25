package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EnterpriseVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IOaEnterpriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2024-05-18 19:47:13
 */
@RestController
@Tag(name = "企业信息")
public class OaEnterpriseController {

    @Resource
    private IOaEnterpriseService enterpriseService;

    @Operation(summary = "分页查询")
    @PostMapping("/oa/enterprise/page/query")
    @SaCheckPermission("oa:enterprise:query")
    public Result<PagedVO<EnterpriseVO>> queryByPage(@RequestBody @Valid EnterpriseQueryDTO queryForm) {
        return Result.success(enterpriseService.queryByPage(queryForm));
    }

    @Operation(summary = "添加")
    @PostMapping("/oa/enterprise/create")
    @SaCheckPermission("oa:enterprise:add")
    public Result<Boolean> add(@RequestBody @Valid EnterpriseAddDTO dto) {
         enterpriseService.add(dto);
         return Result.success(true);
    }

    @Operation(summary = "编辑")
    @PostMapping("/oa/enterprise/update")
    @SaCheckPermission("oa:enterprise:update")
    public Result<Boolean> updateEnterprise(@RequestBody @Valid EnterpriseUpdateDTO dto) {
        enterpriseService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/oa/enterprise/delete/{enterpriseId}")
    @SaCheckPermission("oa:enterprise:delete")
    public Result<Boolean> deleteEnterprise(@PathVariable Integer enterpriseId) {
        enterpriseService.delete(enterpriseId);
        return Result.success(true);
    }
}
