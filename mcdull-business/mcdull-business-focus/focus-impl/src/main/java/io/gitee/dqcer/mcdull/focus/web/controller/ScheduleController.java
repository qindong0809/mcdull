package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.focus.model.entity.AppScheduleEntity;
import io.gitee.dqcer.mcdull.focus.web.service.IScheduleService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
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
public class ScheduleController extends BasicController {

    @Resource
    private IScheduleService scheduleService;

    @Operation(summary = "schedule list")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/list")
    public Result<List<AppScheduleEntity>> list(@RequestBody Map<String, Object> param) {
        LogHelp.info(log, "schedule list param: {}", param);
        List<AppScheduleEntity> list = scheduleService.listByUser(UserContextHolder.userId(),
            parseDate(param.get("startTime")), parseDate(param.get("endTime")));
        return Result.success(list);
    }

    @Operation(summary = "schedule create")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/create")
    public Result<Void> create(@RequestBody AppScheduleEntity param) {
        param.setLocation(CharSequenceUtil.blankToDefault(param.getLocation(), ""));
        param.setCreatedBy(UserContextHolder.userId());
        scheduleService.save(param);
        return Result.success();
    }

    @Operation(summary = "schedule update")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/update")
    public Result<Void> update(@RequestBody AppScheduleEntity param) {
        param.setUpdatedBy(UserContextHolder.userId());
        param.setLocation(CharSequenceUtil.blankToDefault(param.getLocation(), ""));
        scheduleService.updateById(param);
        return Result.success();
    }

    @Operation(summary = "schedule delete")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/delete")
    public Result<Void> delete(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        scheduleService.deleteOwnById(id, UserContextHolder.userId());
        return Result.success();
    }

    private Date parseDate(Object value) {
        return value == null ? null : DateUtil.parse(value.toString());
    }
}
