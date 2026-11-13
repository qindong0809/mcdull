package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitRecordEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppFocusMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitRecordMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppTaskMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ViewController {

    @Resource
    private AppFocusMapper appFocusMapper;
    @Resource
    private AppHabitMapper appHabitMapper;
    @Resource
    private AppHabitRecordMapper appHabitRecordMapper;
    @Resource
    private AppTaskMapper appTaskMapper;

    /**
     * 获取统计概览
     */

    @Operation(summary = "stats overview")
    @PostMapping(GlobalConstant.APP_PATH + "/stats/overview")
    public Result<Map<String, Object>> statsOverview(@RequestBody Map<String, Object> param) {
        String period = (String) param.get("period");

        // 获取当前用户 ID
        Integer userId = UserContextHolder.userId();

        // 计算日期范围
        Date now = new Date();
        Date periodStartDate;
        Date periodEndDate;
        Date lastPeriodStartDate;
        Date lastPeriodEndDate;
        int daysCount;

        if ("week".equals(period)) {
            // 周模式
            periodStartDate = DateUtil.beginOfWeek(now);
            periodEndDate = DateUtil.endOfWeek(now);
            lastPeriodStartDate = DateUtil.offsetWeek(periodStartDate, -1);
            lastPeriodEndDate = DateUtil.offsetDay(periodStartDate, -1);
            daysCount = 7;
        } else {
            // 月模式（默认）
            periodStartDate = DateUtil.beginOfMonth(now);
            periodEndDate = DateUtil.endOfMonth(now);
            lastPeriodStartDate = DateUtil.offsetMonth(periodStartDate, -1);
            lastPeriodEndDate = DateUtil.offsetDay(periodStartDate, -1);
            int value = DateUtil.monthEnum(periodStartDate).getValue();
            daysCount = DateUtil.lengthOfMonth(value, DateUtil.isLeapYear(DateUtil.year(now)));
        }

        // 1. 查询本期任务完成情况
        LambdaQueryWrapper<AppTaskEntity> taskQuery = Wrappers.lambdaQuery();
        taskQuery.eq(AppTaskEntity::getCreatedBy, userId);
        taskQuery.between(AppTaskEntity::getDueDate, periodStartDate, periodEndDate);
        List<AppTaskEntity> currentTasks = appTaskMapper.selectList(taskQuery);

        // 2. 查询上期专注时长
        LambdaQueryWrapper<AppFocusEntity> lastFocusQuery = Wrappers.lambdaQuery();
        lastFocusQuery.eq(AppFocusEntity::getCreatedBy, userId);
        lastFocusQuery.between(AppFocusEntity::getCreatedTime, lastPeriodStartDate, lastPeriodEndDate);
        List<AppFocusEntity> lastFocusRecords = appFocusMapper.selectList(lastFocusQuery);
        int lastPeriodFocusMinutes = lastFocusRecords.stream()
            .mapToInt(entity -> ObjectUtil.defaultIfNull(entity.getDurationMinutes(), 0))
            .sum();

        // 3. 查询本期专注时长
        LambdaQueryWrapper<AppFocusEntity> currentFocusQuery = Wrappers.lambdaQuery();
        currentFocusQuery.eq(AppFocusEntity::getCreatedBy, userId);
        currentFocusQuery.between(AppFocusEntity::getCreatedTime, periodStartDate, periodEndDate);
        List<AppFocusEntity> currentFocusRecords = appFocusMapper.selectList(currentFocusQuery);
        int focusMinutes = currentFocusRecords.stream()
            .mapToInt(entity -> ObjectUtil.defaultIfNull(entity.getDurationMinutes(), 0))
            .sum();

        // 4. 计算任务完成率
        int completionRate = 0;
        if (CollUtil.isNotEmpty(currentTasks)) {
            long completedCount = currentTasks.stream().filter(AppTaskEntity::getCompleted).count();
            completionRate = (int) ((completedCount * 100) / currentTasks.size());
        }

        // 5. 计算连续打卡天数
        int streakDays = calculateStreakDays(userId, periodStartDate);

        // 6. 统计每日完成任务数
        List<Integer> dailyCompletions = new ArrayList<>();
        for (int i = 0; i < daysCount; i++) {
            Date dayStart;
            if ("week".equals(period)) {
                dayStart = DateUtil.offsetDay(periodStartDate, i);
            } else {
                dayStart = DateUtil.offsetDay(periodStartDate, i);
            }
            Date dayEnd = DateUtil.endOfDay(dayStart);

            LambdaQueryWrapper<AppTaskEntity> dayTaskQuery = Wrappers.lambdaQuery();
            dayTaskQuery.eq(AppTaskEntity::getCreatedBy, userId);
            dayTaskQuery.between(AppTaskEntity::getDueDate, dayStart, dayEnd);
            dayTaskQuery.eq(AppTaskEntity::getCompleted, true);

            long count = appTaskMapper.selectCount(dayTaskQuery);
            dailyCompletions.add((int) count);
        }

        // 构建返回结果
        Map<String, Object> result = Map.of(
            "completionRate", completionRate,
            "focusMinutes", focusMinutes,
            "lastPeriodFocusMinutes", lastPeriodFocusMinutes,
            "streakDays", streakDays,
            "dailyCompletions", dailyCompletions
        );

        return Result.success(result);
    }

    /**
     * 年度坚持热力图（VIP 专属）
     * }
     */
    @Operation(summary = "年度坚持热力图")
    @PostMapping(GlobalConstant.APP_PATH + "/stats/heatmap")
    public Result<Map<String, Object>> heatmap(@RequestBody Map<String, Object> param) {
        Integer year = Convert.toInt(param.get("year"), DateUtil.year(new Date()));
        Integer userId = UserContextHolder.userId();

        // 计算年度日期范围
        Date yearStart = DateUtil.parse(year + "-01-01");
        Date yearEnd = DateUtil.endOfDay(DateUtil.parse(year + "-12-31"));

        // 1. 查询本年度习惯打卡记录
        LambdaQueryWrapper<AppHabitRecordEntity> habitQuery = Wrappers.lambdaQuery();
        habitQuery.eq(BaseEntity::getCreatedBy, userId);
        habitQuery.between(AppHabitRecordEntity::getCreatedTime, yearStart, yearEnd);
        List<AppHabitRecordEntity> habitRecords = appHabitRecordMapper.selectList(habitQuery);

        // 按天聚合习惯打卡数
        Map<String, Integer> dailyMap = new HashMap<>();
        for (AppHabitRecordEntity record : habitRecords) {
            String day = DateUtil.formatDate(record.getCreatedTime());
            dailyMap.merge(day, 1, Integer::sum);
        }

        // 2. 查询本年度已完成任务
        LambdaQueryWrapper<AppTaskEntity> taskQuery = Wrappers.lambdaQuery();
        taskQuery.eq(AppTaskEntity::getCreatedBy, userId);
        taskQuery.eq(AppTaskEntity::getCompleted, true);
        taskQuery.between(AppTaskEntity::getUpdatedTime, yearStart, yearEnd);
        List<AppTaskEntity> completedTasks = appTaskMapper.selectList(taskQuery);

        for (AppTaskEntity task : completedTasks) {
            Date dateField = task.getUpdatedTime() != null ? task.getUpdatedTime() : task.getDueDate();
            if (dateField != null) {
                String day = DateUtil.formatDate(dateField);
                dailyMap.merge(day, 1, Integer::sum);
            }
        }

        // 3. 查询本年度专注记录
        LambdaQueryWrapper<AppFocusEntity> focusQuery = Wrappers.lambdaQuery();
        focusQuery.eq(AppFocusEntity::getCreatedBy, userId);
        focusQuery.between(AppFocusEntity::getCreatedTime, yearStart, yearEnd);
        List<AppFocusEntity> focusRecords = appFocusMapper.selectList(focusQuery);

        for (AppFocusEntity focus : focusRecords) {
            String day = DateUtil.formatDate(focus.getCreatedTime());
            dailyMap.merge(day, 1, Integer::sum);
        }

        // 4. 计算统计摘要
        int totalActiveDays = dailyMap.size();
        int totalContributions = dailyMap.values().stream().mapToInt(Integer::intValue).sum();

        // 计算最长连续活跃天数
        int maxStreak = calculateMaxStreak(dailyMap, year);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>(dailyMap);
        result.put("totalActiveDays", totalActiveDays);
        result.put("maxStreak", maxStreak);
        result.put("totalContributions", totalContributions);

        return Result.success(result);
    }

    /**
     * 周报数据（VIP 专属）
     */
    @Operation(summary = "周报数据")
    @PostMapping(GlobalConstant.APP_PATH + "/stats/weeklyReport")
    public Result<Map<String, Object>> weeklyReport(@RequestBody Map<String, Object> param) {
        Integer userId = UserContextHolder.userId();
        Date now = new Date();

        // 本周范围
        Date weekStart = DateUtil.beginOfWeek(now);
        Date weekEnd = DateUtil.endOfWeek(now);
        String weekRange = DateUtil.formatDate(weekStart) + " ~ " + DateUtil.formatDate(weekEnd);

        // 1. 任务统计
        LambdaQueryWrapper<AppTaskEntity> taskQuery = Wrappers.lambdaQuery();
        taskQuery.eq(AppTaskEntity::getCreatedBy, userId);
        taskQuery.between(AppTaskEntity::getDueDate, weekStart, weekEnd);
        List<AppTaskEntity> weekTasks = appTaskMapper.selectList(taskQuery);
        int totalTasks = weekTasks.size();
        long completedTasks = weekTasks.stream().filter(AppTaskEntity::getCompleted).count();
        int completionRate = totalTasks > 0 ? (int) ((completedTasks * 100) / totalTasks) : 0;

        // 2. 专注统计
        LambdaQueryWrapper<AppFocusEntity> focusQuery = Wrappers.lambdaQuery();
        focusQuery.eq(AppFocusEntity::getCreatedBy, userId);
        focusQuery.between(AppFocusEntity::getCreatedTime, weekStart, weekEnd);
        List<AppFocusEntity> weekFocus = appFocusMapper.selectList(focusQuery);
        int focusMinutes = weekFocus.stream()
                .mapToInt(f -> ObjectUtil.defaultIfNull(f.getDurationMinutes(), 0)).sum();
        int focusSessions = weekFocus.size();

        // 3. 习惯打卡统计
        LambdaQueryWrapper<AppHabitRecordEntity> habitQuery = Wrappers.lambdaQuery();
        habitQuery.eq(BaseEntity::getCreatedBy, userId);
        habitQuery.between(AppHabitRecordEntity::getCreatedTime, weekStart, weekEnd);
        List<AppHabitRecordEntity> weekHabitRecords = appHabitRecordMapper.selectList(habitQuery);
        int habitCheckins = weekHabitRecords.size();

        // 4. 最佳习惯（打卡次数最多的）
        String bestHabit = "无";
        if (!weekHabitRecords.isEmpty()) {
            Map<Integer, Long> habitCountMap = weekHabitRecords.stream()
                    .collect(Collectors.groupingBy(AppHabitRecordEntity::getHabitId, Collectors.counting()));
            Integer bestHabitId = habitCountMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(null);
            if (bestHabitId != null) {
                var habit = appHabitMapper.selectById(bestHabitId);
                if (habit != null) bestHabit = habit.getName();
            }
        }

        // 5. 最佳日（完成任务最多的周几）
        String[] dayNames = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int[] dailyCounts = new int[7];
        for (AppTaskEntity t : weekTasks) {
            if (t.getCompleted() && t.getDueDate() != null) {
                int dow = DateUtil.dayOfWeek(t.getDueDate()) - 1;
                if (dow >= 0 && dow < 7) dailyCounts[dow]++;
            }
        }
        int bestDayIndex = 0;
        for (int i = 1; i < 7; i++) {
            if (dailyCounts[i] > dailyCounts[bestDayIndex]) bestDayIndex = i;
        }
        String bestDay = dailyCounts[bestDayIndex] > 0 ? dayNames[bestDayIndex] : "无";

        // 6. 对比上周
        Date lastWeekStart = DateUtil.offsetWeek(weekStart, -1);
        Date lastWeekEnd = DateUtil.offsetDay(weekStart, -1);
        LambdaQueryWrapper<AppTaskEntity> lastTaskQuery = Wrappers.lambdaQuery();
        lastTaskQuery.eq(AppTaskEntity::getCreatedBy, userId);
        lastTaskQuery.between(AppTaskEntity::getDueDate, lastWeekStart, lastWeekEnd);
        List<AppTaskEntity> lastWeekTasks = appTaskMapper.selectList(lastTaskQuery);
        int lastTotal = lastWeekTasks.size();
        long lastCompleted = lastWeekTasks.stream().filter(AppTaskEntity::getCompleted).count();
        int lastRate = lastTotal > 0 ? (int) ((lastCompleted * 100) / lastTotal) : 0;
        int diff = completionRate - lastRate;
        String comparedLastWeek = (diff >= 0 ? "+" : "") + diff + "%";

        // 7. 连续打卡天数
        int streakDays = calculateStreakDays(userId, weekStart);

        Map<String, Object> result = new HashMap<>();
        result.put("weekRange", weekRange);
        result.put("totalTasks", totalTasks);
        result.put("completedTasks", (int) completedTasks);
        result.put("completionRate", completionRate);
        result.put("focusMinutes", focusMinutes);
        result.put("focusSessions", focusSessions);
        result.put("habitCheckins", habitCheckins);
        result.put("bestHabit", bestHabit);
        result.put("bestDay", bestDay);
        result.put("streakDays", streakDays);
        result.put("comparedLastWeek", comparedLastWeek);

        return Result.success(result);
    }

    /**
     * 计算最长连续活跃天数
     */
    private int calculateMaxStreak(Map<String, Integer> dailyMap, int year) {
        if (dailyMap.isEmpty()) return 0;

        List<String> sortedDays = dailyMap.keySet().stream()
            .filter(key -> !key.equals("totalActiveDays") && !key.equals("maxStreak") && !key.equals("totalContributions"))
            .sorted()
            .collect(Collectors.toList());

        if (sortedDays.isEmpty()) return 0;

        int maxStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < sortedDays.size(); i++) {
            Date prev = DateUtil.parse(sortedDays.get(i - 1));
            Date curr = DateUtil.parse(sortedDays.get(i));
            long diffDays = (curr.getTime() - prev.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays == 1) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }

        return maxStreak;
    }

    /**
     * 计算连续打卡天数
     *
     * @param userId 用户 ID
     * @param startDate 起始日期
     * @return 连续天数
     */
    private int calculateStreakDays(Integer userId, Date startDate) {
        int streak = 0;
        Date currentDate = DateUtil.offsetDay(startDate, 0);

        // 从今天往前推，检查每天是否有专注记录或任务完成记录
        while (true) {
            Date dayStart = DateUtil.beginOfDay(currentDate);
            Date dayEnd = DateUtil.endOfDay(currentDate);

            // 检查是否有专注记录
            LambdaQueryWrapper<AppFocusEntity> focusQuery = Wrappers.lambdaQuery();
            focusQuery.eq(AppFocusEntity::getCreatedBy, userId);
            focusQuery.between(AppFocusEntity::getCreatedTime, dayStart, dayEnd);
            long focusCount = appFocusMapper.selectCount(focusQuery);

            // 检查是否有完成任务
            LambdaQueryWrapper<AppTaskEntity> taskQuery = Wrappers.lambdaQuery();
            taskQuery.eq(AppTaskEntity::getCreatedBy, userId);
            taskQuery.between(AppTaskEntity::getCreatedTime, dayStart, dayEnd);
            taskQuery.eq(AppTaskEntity::getCompleted, true);
            long taskCount = appTaskMapper.selectCount(taskQuery);

            if (focusCount > 0 || taskCount > 0) {
                streak++;
                currentDate = DateUtil.offsetDay(currentDate, -1);
            } else {
                break;
            }

            // 最多检查 365 天
            if (streak >= 365) {
                break;
            }
        }

        return streak;
    }
}
