package io.gitee.dqcer.mcdull.blaze.controller;


import cn.hutool.core.lang.Pair;
import io.gitee.dqcer.mcdull.blaze.domain.vo.ReportViewVO;
import io.gitee.dqcer.mcdull.blaze.service.IReportService;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "报表")
public class ReportController {

    @Resource
    private IReportService reportService;

    @Operation(summary = "领导驾驶舱")
    @GetMapping("/report/view")
    public Result<ReportViewVO> getReportView() {
        ReportViewVO vo = reportService.getReportView();
        return Result.success(vo);
    }

    @Operation(summary = "领导驾驶舱-累计匹配成功数")
    @GetMapping("/report/match-success-count-total")
    public Result<Integer> getMatchSuccessCountTotal() {
        Integer count = reportService.getMatchSuccessCountTotal();
        return Result.success(count);
    }

    @Operation(summary = "领导驾驶舱-企业证书需求待匹配数")
    @GetMapping("/report/enterprise-certificate-count-total")
    public Result<Integer> getEnterpriseCertificateDemandPendingMatchCount() {
        Integer count = reportService.getEnterpriseCertificateDemandPendingMatchCount();
        return Result.success(count);
    }

    @Operation(summary = "领导驾驶舱-人才证书待匹配数")
    @GetMapping("/report/talent-certificate-count-total")
    public Result<Integer> getTalentCertificatePendingMatchCount() {
        Integer count = reportService.getTalentCertificatePendingMatchCount();
        return Result.success(count);
    }

    @Operation(summary = "领导驾驶舱-累计企业、人才数")
    @GetMapping("/report/enterprise-talent-count-total")
    public Result<Pair<Integer, Integer>> getEnterpriseTalentCountTotal() {
        Pair<Integer, Integer> count = reportService.getEnterpriseTalentCountTotal();
        return Result.success(count);
    }




}
