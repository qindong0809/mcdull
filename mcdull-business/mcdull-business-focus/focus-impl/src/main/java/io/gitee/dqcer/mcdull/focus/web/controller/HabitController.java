package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitEntity;
import io.gitee.dqcer.mcdull.focus.web.service.IHabitService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class HabitController extends BasicController {

    @Resource
    private IHabitService habitService;

    @Operation(summary = "创建习惯")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/create")
    public Result<Boolean> create(@RequestBody Map<String, Object> request) {
        AppHabitEntity entity = new AppHabitEntity();
        entity.setName(Convert.toStr(request.get("name")));
        entity.setReminderTime(Convert.toStr(request.get("reminderTime")));
        entity.setPreset(Convert.toBool(request.get("preset")));
        habitService.save(entity);
        return Result.success(true);
    }

    @Operation(summary = "更新习惯")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/update")
    public Result<Boolean> update(@RequestBody Map<String, Object> request) {
        AppHabitEntity entity = new AppHabitEntity();
        entity.setId(Convert.toInt(request.get("id")));
        entity.setName(Convert.toStr(request.get("name")));
        entity.setReminderTime(Convert.toStr(request.get("reminderTime")));
        entity.setPreset(Convert.toBool(request.get("preset")));
        habitService.updateById(entity);
        return Result.success(true);
    }

    @Operation(summary = "删除习惯")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/delete")
    public Result<Boolean> delete(@RequestBody Map<String, Object> request) {
        habitService.deleteById(Convert.toInt(request.get("id")));
        return Result.success(true);
    }

    @Operation(summary = "获取习惯列表")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/list")
    public Result<List<Map<String, Object>>> list() {
        Integer userId = UserContextHolder.userId();
        List<AppHabitEntity> list = habitService.listByUser(userId);
        Set<Integer> checkedHabitIds = habitService.listHabitIdsCheckedToday(userId);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (AppHabitEntity entity : list) {
            mapList.add(Map.of(
                "id", entity.getId(),
                "name", entity.getName(),
                "reminderTime", entity.getReminderTime(),
                "preset", entity.getPreset(),
                "checkedToday", checkedHabitIds.contains(entity.getId())
            ));
        }
        return Result.success(mapList);
    }

    @Operation(summary = "习惯打卡")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/checkin")
    public Result<Boolean> checkin(@RequestBody Map<String, Object> request) {
        habitService.checkIn(Convert.toInt(request.get("id")));
        return Result.success(true);
    }


    @Operation(summary = "习惯取消打卡")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/uncheck")
    public Result<Boolean> uncheck(@RequestBody Map<String, Object> request) {
        habitService.cancelTodayCheckIn(Convert.toInt(request.get("id")), UserContextHolder.userId());
        return Result.success(true);
    }


    @Operation(summary = "补签卡")
    @PostMapping("/app/habit/makeUp")
    public Result<Map<String, Object>> makeUpCheckIn(@RequestBody Map<String, Object> request) {
        Date date = Convert.toDate(request.get("date"));
        int streakDays = habitService.makeUpCheckIn(Convert.toInt(request.get("habitId")), date, UserContextHolder.userId());
        return Result.success(Map.of(
            "ok", true,
            "data", Map.of(
                "streakDays", streakDays
            )
        ));
    }
}
