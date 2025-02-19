package io.gitee.dqcer.blaze.controller;

import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.blaze.domain.bo.CertificateBO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateAddDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.TalentCertificateVO;
import io.gitee.dqcer.blaze.service.ITalentCertificateService;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.util.CertificateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@RestController
@Tag(name = "人才证书需求表")
public class TalentCertificateController extends BasicController {

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
        return Result.success(talentCertificateService.list(customerCertId));
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
        return Result.success(talentCertificateService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @PostMapping(value = "/talent-cert/list/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData() {
        talentCertificateService.exportData();
    }

    @Operation(summary = "下载模板")
    @PostMapping(value = "/talent-cert/list/download-template", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTemplate() {
        talentCertificateService.downloadTemplate();
    }

    @Operation(summary = "添加")
    @PostMapping(value ="/talent-cert/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> add(@Valid TalentCertificateAddDTO dto, @RequestPart("file") MultipartFile file) {
        talentCertificateService.insert(dto, file);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping(value = "/talent-cert/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> update( @Valid TalentCertificateUpdateDTO dto, @RequestPart("file") MultipartFile file) {
        talentCertificateService.update(dto, file);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/talent-cert/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        talentCertificateService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/talent-cert/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        talentCertificateService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }
}
