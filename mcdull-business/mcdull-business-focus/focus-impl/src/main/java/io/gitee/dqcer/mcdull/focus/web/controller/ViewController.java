package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import io.gitee.dqcer.mcdull.focus.web.service.IStatsService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class ViewController {

    @Resource
    private IStatsService statsService;

    @Operation(summary = "stats overview")
    @PostMapping(GlobalConstant.APP_PATH + "/stats/overview")
    public Result<Map<String, Object>> statsOverview(@RequestBody Map<String, Object> param) {
        Integer userId = UserContextHolder.userId();
        return Result.success(statsService.overview(userId, Convert.toStr(param.get("period"))));
    }

    @Operation(summary = "年度坚持热力图")
    @PostMapping(GlobalConstant.APP_PATH + "/stats/heatmap")
    public Result<Map<String, Object>> heatmap(@RequestBody Map<String, Object> param) {
        Integer userId = UserContextHolder.userId();
        Integer year = Convert.toInt(param.get("year"), DateUtil.year(new Date()));
        return Result.success(statsService.heatmap(userId, year));
    }

    @Operation(summary = "周报数据")
    @PostMapping(GlobalConstant.APP_PATH + "/stats/weeklyReport")
    public Result<Map<String, Object>> weeklyReport(@RequestBody Map<String, Object> param) {
        return Result.success(statsService.weeklyReport(UserContextHolder.userId()));
    }
}
