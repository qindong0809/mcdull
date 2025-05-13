package io.gitee.dqcer.mcdull.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.blaze.domain.bo.CertificateBO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.TalentCertificateVO;
import io.gitee.dqcer.mcdull.blaze.service.ITalentCertificateService;
import io.gitee.dqcer.mcdull.blaze.util.CertificateUtil;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@RestController
@Tag(name = "人才证书需求表")
public class TalentCertificateController extends BlazeBasicController {

    @Resource
    private ITalentCertificateService talentCertificateService;


    @Operation(summary = "人才证书级别")
    @GetMapping("/talent-cert/getCertificateLevelList")
    public Result<List<LabelValueVO<Integer, String>>> getCertificateLevelList() {
        Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
        List<LabelValueVO<Integer, String> > list = certificateMap.values().stream()
                .map(v -> new LabelValueVO<>(v.getCode(), v.getName())).collect(Collectors.toList());
        return Result.success(list);
    }

    @Operation(summary = "全部数据")
    @GetMapping("/talent-cert/list/{customerCertId}")
    public Result<List<LabelValueVO<Integer, String>>> list(@PathVariable(value = "customerCertId") Integer customerCertId) {
        return Result.success(talentCertificateService.list(customerCertId, true));
    }

    @Operation(summary = "公共专业")
    @GetMapping("/pub/CertificateLevelList")
    public Result<List<LabelValueVO<Integer, String>>> getPubCertificateLevelList() {
        List<CertificateBO> certificate = CertificateUtil.getCertificate();
        return Result.success(certificate.stream()
                .map(v -> new LabelValueVO<>(Convert.toInt(v.getCode()), v.getName())).collect(Collectors.toList()));
    }

    @Operation(summary = "公共职称")
    @GetMapping("/pub/majorList")
    public Result<List<LabelValueVO<Integer, String>>> getPubMajorList() {
        List<CertificateBO> certificate = CertificateUtil.getCertificate();
        List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
        for (CertificateBO certificateBO : certificate) {
            List<CertificateBO.Major> majorList = certificateBO.getMajorList();
            for (CertificateBO.Major major : majorList) {
                voList.add(new LabelValueVO<>(major.getCode(), StrUtil.format("({}){}", certificateBO.getName(), major.getName())));
            }
        }
        return Result.success(voList);
    }

    @Operation(summary = "专业")
    @GetMapping("/talent-cert/getMajorList/{code}")
    public Result<List<LabelValueVO<Integer, String>>> getMajorList(@PathVariable(value = "code") Integer code) {
        Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
        return Result.success(certificateMap.get(code).getMajorList().stream()
                .map(v -> new LabelValueVO<>(v.getCode(), v.getName())).collect(Collectors.toList()));
    }

    @Operation(summary = "分页")
    @PostMapping("/talent-cert/queryPage")
    public Result<PagedVO<TalentCertificateVO>> queryPage(@RequestBody @Valid TalentCertificateQueryDTO dto) {
        PagedVO<TalentCertificateVO> pagedVO = super.executeByPermission("blaze:talent_certificate:approve", PageUtil.empty(dto), dto,
                r -> talentCertificateService.queryPage(r));
        return Result.success(pagedVO);
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("blaze:talent_certificate:export")
    @PostMapping(value = "/talent-cert/list/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid TalentCertificateQueryDTO dto) {
       super.executeByPermission("blaze:talent_certificate:approve", true, dto,
                r -> talentCertificateService.exportData(r));
    }

    @Operation(summary = "下载模板")
    @PostMapping(value = "/talent-cert/list/download-template", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTemplate() {
        talentCertificateService.downloadTemplate();
    }

    @Operation(summary = "添加")
    @SaCheckPermission("blaze:talent_certificate:write")
    @PostMapping(value ="/talent-cert/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> add(@Valid TalentCertificateAddDTO dto, @RequestPart(value = "file", required = false) List<MultipartFile> fileList) {
        talentCertificateService.insert(dto, fileList);
        return Result.success(true);
    }

    @Operation(summary = "复制")
    @SaCheckPermission("blaze:talent_certificate:write")
    @PostMapping(value = "/talent-cert/{id}/copy")
    public Result<Integer> copy(@PathVariable(value = "id") Integer id) {
        return Result.success(super.locker(null,  () -> talentCertificateService.copy(id)));
    }

    @Operation(summary = "更新")
    @SaCheckPermission("blaze:talent_certificate:write")
    @PostMapping(value = "/talent-cert/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> update(@Valid TalentCertificateUpdateDTO dto, @RequestPart(value = "file", required = false) List<MultipartFile> fileList) {
        talentCertificateService.update(dto, fileList);
        return Result.success(true);
    }

    @Operation(summary = "审批")
    @SaCheckPermission("blaze:talent_certificate:approve")
    @PostMapping("/talent-cert/approve")
    public Result<Boolean> approve(@RequestBody @Valid ApproveDTO dto) {
        talentCertificateService.approve(dto);
        return Result.success(true);
    }


    @Operation(summary = "批量删除")
    @SaCheckPermission("blaze:talent_certificate:write")
    @PostMapping("/talent-cert/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        talentCertificateService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @SaCheckPermission("blaze:talent_certificate:write")
    @GetMapping("/talent-cert/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        talentCertificateService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }
}
