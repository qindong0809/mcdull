package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppFocusMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class FocusController extends BasicController {
    @Resource
    private AppFocusMapper appFocusMapper;


    @Operation(summary = "保存专注记录")
    @PostMapping(GlobalConstant.APP_PATH + "/focus/save")
    public Result<Boolean> create(@RequestBody Map<String, Object> request) {
        AppFocusEntity entity = new AppFocusEntity();
        entity.setDurationMinutes(Convert.toInt(request.get("durationMinutes")));
        entity.setMode(Convert.toInt(request.get("mode")));
        entity.setTaskId(Convert.toInt(request.get("preset")));
        appFocusMapper.insert(entity);
        return Result.success(true);
    }


    @Operation(summary = "获取专注历史记录")
    @PostMapping(GlobalConstant.APP_PATH + "/focus/history")
    public Result<List<AppFocusEntity>> getFocusHistory(@RequestBody Map<String, Object> request) {
        LambdaQueryWrapper<AppFocusEntity> query = Wrappers.lambdaQuery();
        Object startTime = request.get("startDate");
        if (startTime != null) {
            query.ge(AppFocusEntity::getCreatedTime, DateUtil.beginOfDay(DateUtil.parse(startTime.toString())));
        }
        Object endTime = request.get("endDate");
        if (endTime != null) {
            query.le(AppFocusEntity::getCreatedTime, DateUtil.endOfDay(DateUtil.parse(endTime.toString())));
        }
        Page<AppFocusEntity> mapPage = appFocusMapper.selectPage(new Page<>(Convert.toInt(request.get("page")), Convert.toInt(request.get("size"))), query);
        return Result.success(mapPage.getRecords());
    }
}
