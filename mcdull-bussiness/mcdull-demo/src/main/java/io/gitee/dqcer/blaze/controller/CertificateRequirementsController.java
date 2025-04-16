package io.gitee.dqcer.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.blaze.domain.bo.CertificateBO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsAddDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.util.CertificateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@RestController
@Tag(name = "证书需求表")
public class CertificateRequirementsController extends BasicController {

    @Resource
    private ICertificateRequirementsService certificateRequirementsService;

    @Operation(summary = "证书级别")
    @GetMapping("/CertificateRequirements/getCertificateLevelList")
    public Result<List<LabelValueVO<Integer, String>>> getCertificateLevelList() {
        Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
        List<LabelValueVO<Integer, String> > list = certificateMap.values().stream()
                .map(v -> new LabelValueVO<>(v.getCode(), v.getName())).collect(Collectors.toList());
        return Result.success(list);
    }

    @Operation(summary = "全部数据")
    @PostMapping("/CertificateRequirements/list")
    public Result<List<LabelValueVO<Integer, String>>> list() {
        return Result.success(certificateRequirementsService.all(true));
    }

    @Operation(summary = "专业")
    @GetMapping("/CertificateRequirements/getMajorList/{code}")
    public Result<List<LabelValueVO<Integer, String>>> getMajorList(@PathVariable(value = "code") Integer code) {
        Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
        return Result.success(certificateMap.get(code).getMajorList().stream()
                .map(v -> new LabelValueVO<>(v.getCode(), v.getName())).collect(Collectors.toList()));
    }

    @Operation(summary = "分页")
    @PostMapping("/CertificateRequirements/queryPage")
    public Result<PagedVO<CertificateRequirementsVO>> queryPage(@RequestBody @Valid CertificateRequirementsQueryDTO dto) {
        return Result.success(certificateRequirementsService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("blaze:certificate_requirements:export")
    @PostMapping(value = "CertificateRequirements/list/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid CertificateRequirementsQueryDTO dto) {
        certificateRequirementsService.exportData(dto);
    }

    @Operation(summary = "导出数据企业pdf")
    @SaCheckPermission("blaze:certificate_requirements:export")
    @PostMapping(value = "/{customerId}/blazeOrder/record-export-pdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void getExportCustomerOrderDetailPdf(@PathVariable(value = "customerId") Integer customerId) throws IOException {
        certificateRequirementsService.getExportCustomerOrderDetailPdf(customerId);
    }

    @Operation(summary = "下载模板")
    @PostMapping(value = "CertificateRequirements/list/download-template", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTemplate() {
        certificateRequirementsService.downloadTemplate();
    }

    @Operation(summary = "添加")
    @SaCheckPermission("blaze:certificate_requirements:write")
    @PostMapping(value = "/CertificateRequirements/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> add(@Valid CertificateRequirementsAddDTO dto,
                               @RequestPart(value = "file", required = false) List<MultipartFile> fileList) {
        certificateRequirementsService.insert(dto, fileList);
        return Result.success(true);
    }

    @Operation(summary = "复制")
    @SaCheckPermission("blaze:certificate_requirements:write")
    @PostMapping(value = "/CertificateRequirements/{id}/copy")
    public Result<Integer> copy(@PathVariable(value = "id") Integer id) {
        return Result.success(super.locker(null,  () -> certificateRequirementsService.copy(id)));
    }

    @Operation(summary = "审批")
    @SaCheckPermission("blaze:certificate_requirements:approve")
    @PostMapping("/CertificateRequirements/approve")
    public Result<Boolean> approve(@RequestBody @Valid ApproveDTO dto) {
        certificateRequirementsService.approve(dto);
        return Result.success(true);
    }


    @Operation(summary = "更新")
    @SaCheckPermission("blaze:certificate_requirements:write")
    @PostMapping(value = "/CertificateRequirements/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> update(@Valid CertificateRequirementsUpdateDTO dto,
                                  @RequestPart(value = "file", required = false) List<MultipartFile> fileList) {
        certificateRequirementsService.update(dto, fileList);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @SaCheckPermission("blaze:certificate_requirements:write")
    @PostMapping("/CertificateRequirements/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        certificateRequirementsService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @SaCheckPermission("blaze:certificate_requirements:write")
    @GetMapping("/CertificateRequirements/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        certificateRequirementsService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }
}
