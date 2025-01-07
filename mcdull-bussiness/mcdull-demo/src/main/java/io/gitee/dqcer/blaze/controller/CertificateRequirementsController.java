package io.gitee.dqcer.blaze.controller;

import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsAddDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@RestController
@Tag(name = "证书需求表")
public class CertificateRequirementsController extends BasicController {

    @Resource
    private ICertificateRequirementsService blazeCertificateRequirementsService;

    @Operation(summary = "分页")
    @PostMapping("/CertificateRequirements/queryPage")
    public Result<PagedVO<CertificateRequirementsVO>> queryPage(@RequestBody @Valid CertificateRequirementsQueryDTO dto) {
        return Result.success(blazeCertificateRequirementsService.queryPage(dto));
    }

    @Operation(summary = "添加")
    @PostMapping("/CertificateRequirements/add")
    public Result<Boolean> add(@RequestBody @Valid CertificateRequirementsAddDTO dto) {
        blazeCertificateRequirementsService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping("/CertificateRequirements/update")
    public Result<Boolean> update(@RequestBody @Valid CertificateRequirementsUpdateDTO dto) {
        blazeCertificateRequirementsService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/CertificateRequirements/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        blazeCertificateRequirementsService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/CertificateRequirements/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable Integer id) {
        blazeCertificateRequirementsService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }
}
