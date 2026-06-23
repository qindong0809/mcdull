package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppScheduleEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppScheduleMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ScheduleController extends BasicController {
    @Resource
    private AppScheduleMapper appScheduleMapper;


    @Operation(summary = "schedule list")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/list")
    public Result<List<AppScheduleEntity>> list(@RequestBody Map<String, Object> param) {
        LambdaQueryWrapper<AppScheduleEntity> query = Wrappers.lambdaQuery();
        // 当日，本周，本月进行过滤筛选
        LogHelp.info(log, "schedule list param: {}", param);
        Object startTime = param.get("startTime");
        if (ObjectUtil.isNotNull(startTime)) {
            query.ge(AppScheduleEntity::getStartTime, DateUtil.beginOfDay(DateUtil.parse(startTime.toString())));
        }
        Object endTime = param.get("endTime");
        if (ObjectUtil.isNotNull(endTime)) {
            query.le(AppScheduleEntity::getEndTime, DateUtil.endOfDay(DateUtil.parse(endTime.toString())));
        }
        query.eq(AppScheduleEntity::getCreatedBy, UserContextHolder.userId());
        query.orderByAsc(AppScheduleEntity::getCreatedBy);
        return Result.success(appScheduleMapper.selectList(query));
    }

    @Operation(summary = "schedule create")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/create")
    public Result<Void> create(@RequestBody AppScheduleEntity param) {
        param.setLocation(CharSequenceUtil.blankToDefault(param.getLocation(), ""));
        param.setCreatedBy(UserContextHolder.userId());
        appScheduleMapper.insert(param);
        return Result.success();
    }

    @Operation(summary = "schedule update")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/update")
    public Result<Void> update(@RequestBody AppScheduleEntity param) {
        param.setUpdatedBy(UserContextHolder.userId());
        param.setLocation(CharSequenceUtil.blankToDefault(param.getLocation(), ""));
        appScheduleMapper.updateById(param);
        return Result.success();
    }

    @Operation(summary = "schedule delete")
    @PostMapping(GlobalConstant.APP_PATH + "/schedule/delete")
    public Result<Void> delete(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        appScheduleMapper.deleteById(id);
        return Result.success();
    }
}
