package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EnterpriseVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IOaEnterpriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author dqcer
 * @since 2024-05-18 19:47:13
 */
@RestController
@Tag(name = "企业信息")
public class OaEnterpriseController extends BasicController {

    @Resource
    private IOaEnterpriseService enterpriseService;

    @Operation(summary = "分页查询")
    @PostMapping("/oa/enterprise/page/query")
    @SaCheckEL("stp.checkPermission('oa:enterprise:query')")
    public Result<PagedVO<EnterpriseVO>> queryByPage(@RequestBody @Valid EnterpriseQueryDTO queryForm) {
        return Result.success(enterpriseService.queryByPage(queryForm));
    }

    @Operation(summary = "导出数据")
    @SaCheckEL("stp.checkPermission('system:enterprise:export')")
    @PostMapping(value = "/system/enterprise/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid EnterpriseQueryDTO dto) {
        enterpriseService.exportData(dto);
    }

    @Operation(summary = "添加")
    @PostMapping("/oa/enterprise/create")
    @SaCheckEL("stp.checkPermission('oa:enterprise:add')")
    public Result<Boolean> add(@RequestBody @Valid EnterpriseAddDTO dto) {
         enterpriseService.add(dto);
         return Result.success(true);
    }

    @Operation(summary = "查询企业详情")
    @GetMapping("/oa/enterprise/get/{enterpriseId}")
    @SaCheckEL("stp.checkPermission('oa:enterprise:detail')")
    public Result<EnterpriseVO> getDetail(@PathVariable(value = "enterpriseId") Integer enterpriseId) {
        return Result.success(enterpriseService.getDetail(enterpriseId));
    }

    @Operation(summary = "编辑")
    @PostMapping("/oa/enterprise/update")
    @SaCheckEL("stp.checkPermission('oa:enterprise:update')")
    public Result<Boolean> updateEnterprise(@RequestBody @Valid EnterpriseUpdateDTO dto) {
        enterpriseService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/oa/enterprise/delete/{enterpriseId}")
    @SaCheckEL("stp.checkPermission('oa:enterprise:delete')")
    public Result<Boolean> deleteEnterprise(@PathVariable(value = "enterpriseId") Integer enterpriseId) {
        enterpriseService.delete(enterpriseId);
        return Result.success(true);
    }
}
