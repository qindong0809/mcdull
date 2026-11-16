package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;
import io.gitee.dqcer.mcdull.focus.web.service.IFocusService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class FocusController extends BasicController {

    @Resource
    private IFocusService focusService;

    @Operation(summary = "保存专注记录")
    @PostMapping(GlobalConstant.APP_PATH + "/focus/save")
    public Result<Boolean> create(@RequestBody Map<String, Object> request) {
        AppFocusEntity entity = new AppFocusEntity();
        entity.setDurationMinutes(Convert.toInt(request.get("durationMinutes")));
        entity.setMode(Convert.toInt(request.get("mode")));
        entity.setTaskId(Convert.toInt(request.get("taskId")));
        focusService.save(entity);
        return Result.success(true);
    }


    @Operation(summary = "获取专注历史记录")
    @PostMapping(GlobalConstant.APP_PATH + "/focus/history")
    public Result<List<AppFocusEntity>> getFocusHistory(@RequestBody Map<String, Object> request) {
        List<AppFocusEntity> records = focusService.pageByCreatedTime(
            parseDate(request.get("startDate")),
            parseDate(request.get("endDate")),
            Convert.toInt(request.get("page")),
            Convert.toInt(request.get("size")));
        return Result.success(records);
    }

    private Date parseDate(Object value) {
        return value == null ? null : DateUtil.parse(value.toString());
    }
}
