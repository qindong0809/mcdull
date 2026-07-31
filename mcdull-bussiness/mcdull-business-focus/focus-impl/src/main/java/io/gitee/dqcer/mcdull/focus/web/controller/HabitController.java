package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitRecordEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitRecordMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class HabitController extends BasicController {

    @Resource
    private AppHabitMapper appHabitMapper;
    @Resource
    private AppHabitRecordMapper appHabitRecordMapper;

    @Operation(summary = "创建习惯")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/create")
    public Result<Boolean> create(@RequestBody Map<String, Object> request) {
        AppHabitEntity entity = new AppHabitEntity();
        entity.setName(Convert.toStr(request.get("name")));
        entity.setReminderTime(Convert.toStr(request.get("reminderTime")));
        entity.setPreset(Convert.toBool(request.get("preset")));
        appHabitMapper.insert(entity);
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
        appHabitMapper.updateById(entity);
        return Result.success(true);
    }

    @Operation(summary = "删除习惯")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/delete")
    public Result<Boolean> delete(@RequestBody Map<String, Object> request) {
        appHabitMapper.deleteById(Convert.toInt(request.get("id")));
        return Result.success(true);
    }

    @Operation(summary = "获取习惯列表")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/list")
    public Result<List<Map<String, Object>>> list() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        LambdaQueryWrapper<AppHabitEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getCreatedBy, UserContextHolder.userId());
        List<AppHabitEntity> list = appHabitMapper.selectList(query);
        LambdaQueryWrapper<AppHabitRecordEntity> query1 = Wrappers.lambdaQuery();
        query1.eq(BaseEntity::getCreatedBy, UserContextHolder.userId());
        Map<Integer, List<AppHabitRecordEntity>> recordMap = appHabitRecordMapper.selectList(query1).
            stream().collect(Collectors.groupingBy(AppHabitRecordEntity::getHabitId));
        for (AppHabitEntity entity : list) {
            List<AppHabitRecordEntity> records = recordMap.getOrDefault(entity.getId(), new ArrayList<>());
            boolean checkedToday = records.stream().anyMatch(i -> DateUtil.isSameDay(i.getCreatedTime(), new Date()));
            mapList.add(Map.of(
                "id", entity.getId(),
                "name", entity.getName(),
                "reminderTime", entity.getReminderTime(),
                "preset", entity.getPreset(),
                "checkedToday", checkedToday
            ));
        }
        return Result.success(mapList);
    }

    @Operation(summary = "习惯打卡")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/checkin")
    public Result<Boolean> checkin(@RequestBody Map<String, Object> request) {
        AppHabitRecordEntity entity = new AppHabitRecordEntity();
        entity.setHabitId(Convert.toInt(request.get("id")));
        appHabitRecordMapper.insert(entity);
        return Result.success(true);
    }


    @Operation(summary = "习惯取消打卡")
    @PostMapping(GlobalConstant.APP_PATH + "/habit/uncheck")
    public Result<Boolean> uncheck(@RequestBody Map<String, Object> request) {
        LambdaQueryWrapper<AppHabitRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(AppHabitRecordEntity::getHabitId, Convert.toInt(request.get("id")));
        query.eq(BaseEntity::getCreatedBy, UserContextHolder.userId());
        // 只删除今天的打卡记录，不影响历史
        query.ge(AppHabitRecordEntity::getCreatedTime, DateUtil.beginOfDay(new Date()));
        query.le(AppHabitRecordEntity::getCreatedTime, DateUtil.endOfDay(new Date()));
        appHabitRecordMapper.delete(query);
        return Result.success(true);
    }


    @Operation(summary = "补签卡")
    @PostMapping("/app/habit/makeUp")
    public Result<Map<String, Object>> makeUpCheckIn(@RequestBody Map<String, Object> request) {
        Integer habitId = Convert.toInt(request.get("habitId"));
        AppHabitRecordEntity entity = new AppHabitRecordEntity();
        entity.setHabitId(habitId);
        entity.setCreatedTime(Convert.toDate(request.get("date")));
        appHabitRecordMapper.insert(entity);

        LambdaQueryWrapper<AppHabitRecordEntity> query1 = Wrappers.lambdaQuery();
        query1.eq(AppHabitRecordEntity::getHabitId, habitId);
        query1.eq(BaseEntity::getCreatedBy, UserContextHolder.userId());
        List<AppHabitRecordEntity> records = appHabitRecordMapper.selectList(query1);

        List<DateTime> sortedDays = records.stream()
            .map(AppHabitRecordEntity::getCreatedTime)
            .map(DateUtil::beginOfDay)
            .distinct()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        int max = 0;
        int current = 0;
        DateTime prev = null;

        for (DateTime day : sortedDays) {
            if (prev == null) {
                current = 1;
            } else {
                long between = DateUtil.between(day, prev, DateUnit.DAY);
                if (between == 1) {
                    current++;
                } else {
                    current = 1;
                }
            }
            max = Math.max(max, current);
            prev = day;
        }


        return Result.success(Map.of(
            "ok", true,
            "data", Map.of(
                "streakDays", max
            )
        ));
    }
}
