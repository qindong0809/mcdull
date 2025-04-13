package io.gitee.dqcer.blaze.controller;


import io.gitee.dqcer.blaze.domain.vo.ReportViewVO;
import io.gitee.dqcer.blaze.service.IReportService;
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

}
