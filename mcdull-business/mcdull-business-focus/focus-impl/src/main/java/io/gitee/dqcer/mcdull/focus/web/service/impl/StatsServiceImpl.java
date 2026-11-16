package io.gitee.dqcer.mcdull.focus.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitRecordEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;
import io.gitee.dqcer.mcdull.focus.web.service.IFocusService;
import io.gitee.dqcer.mcdull.focus.web.service.IHabitService;
import io.gitee.dqcer.mcdull.focus.web.service.IStatsService;
import io.gitee.dqcer.mcdull.focus.web.service.ITaskService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计视图服务实现
 *
 * @author dqcer
 */
@Service
public class StatsServiceImpl implements IStatsService {

    private static final String PERIOD_WEEK = "week";

    private static final String[] DAY_NAMES = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    @Resource
    private IFocusService focusService;

    @Resource
    private IHabitService habitService;

    @Resource
    private ITaskService taskService;

    @Override
    public Map<String, Object> overview(Integer userId, String period) {
        Date now = new Date();
        boolean weekly = PERIOD_WEEK.equals(period);
        Date periodStartDate;
        Date periodEndDate;
        Date lastPeriodStartDate;
        Date lastPeriodEndDate;
        int daysCount;
        if (weekly) {
            periodStartDate = DateUtil.beginOfWeek(now);
            periodEndDate = DateUtil.endOfWeek(now);
            lastPeriodStartDate = DateUtil.offsetWeek(periodStartDate, -1);
            lastPeriodEndDate = DateUtil.offsetDay(periodStartDate, -1);
            daysCount = 7;
        } else {
            periodStartDate = DateUtil.beginOfMonth(now);
            periodEndDate = DateUtil.endOfMonth(now);
            lastPeriodStartDate = DateUtil.offsetMonth(periodStartDate, -1);
            lastPeriodEndDate = DateUtil.offsetDay(periodStartDate, -1);
            int month = DateUtil.monthEnum(periodStartDate).getValue();
            daysCount = DateUtil.lengthOfMonth(month, DateUtil.isLeapYear(DateUtil.year(now)));
        }

        // 1. 本期任务完成情况
        List<AppTaskEntity> currentTasks = taskService.listByDueDateRange(userId, periodStartDate, periodEndDate);
        int completionRate = 0;
        if (CollUtil.isNotEmpty(currentTasks)) {
            long completedCount = currentTasks.stream().filter(AppTaskEntity::getCompleted).count();
            completionRate = (int) ((completedCount * 100) / currentTasks.size());
        }

        // 2. 上期与本期的专注时长
        int lastPeriodFocusMinutes = focusService.sumDurationMinutes(userId, lastPeriodStartDate, lastPeriodEndDate);
        int focusMinutes = focusService.sumDurationMinutes(userId, periodStartDate, periodEndDate);

        // 3. 统计每日完成任务数
        List<Integer> dailyCompletions = new ArrayList<>();
        for (int i = 0; i < daysCount; i++) {
            Date dayStart = DateUtil.offsetDay(periodStartDate, i);
            dailyCompletions.add((int) taskService.countCompletedByDueDateRange(userId, dayStart, DateUtil.endOfDay(dayStart)));
        }

        return Map.of(
            "completionRate", completionRate,
            "focusMinutes", focusMinutes,
            "lastPeriodFocusMinutes", lastPeriodFocusMinutes,
            "streakDays", calculateStreakDays(userId, periodStartDate),
            "dailyCompletions", dailyCompletions
        );
    }

    @Override
    public Map<String, Object> heatmap(Integer userId, Integer year) {
        Date yearStart = DateUtil.parse(year + "-01-01");
        Date yearEnd = DateUtil.endOfDay(DateUtil.parse(year + "-12-31"));

        // 按天聚合习惯打卡、已完成任务与专注记录
        Map<String, Integer> dailyMap = new HashMap<>();
        for (AppHabitRecordEntity record : habitService.listRecordsByCreatedTime(userId, yearStart, yearEnd)) {
            dailyMap.merge(DateUtil.formatDate(record.getCreatedTime()), 1, Integer::sum);
        }
        for (AppTaskEntity task : taskService.listCompletedByUpdatedTimeRange(userId, yearStart, yearEnd)) {
            Date dateField = task.getUpdatedTime() != null ? task.getUpdatedTime() : task.getDueDate();
            if (dateField != null) {
                dailyMap.merge(DateUtil.formatDate(dateField), 1, Integer::sum);
            }
        }
        for (AppFocusEntity focus : focusService.listByCreatedTime(userId, yearStart, yearEnd)) {
            dailyMap.merge(DateUtil.formatDate(focus.getCreatedTime()), 1, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>(dailyMap);
        result.put("totalActiveDays", dailyMap.size());
        result.put("maxStreak", calculateMaxStreak(dailyMap));
        result.put("totalContributions", dailyMap.values().stream().mapToInt(Integer::intValue).sum());
        return result;
    }

    @Override
    public Map<String, Object> weeklyReport(Integer userId) {
        Date now = new Date();
        Date weekStart = DateUtil.beginOfWeek(now);
        Date weekEnd = DateUtil.endOfWeek(now);
        String weekRange = DateUtil.formatDate(weekStart) + " ~ " + DateUtil.formatDate(weekEnd);

        // 1. 任务统计
        List<AppTaskEntity> weekTasks = taskService.listByDueDateRange(userId, weekStart, weekEnd);
        int totalTasks = weekTasks.size();
        long completedTasks = weekTasks.stream().filter(AppTaskEntity::getCompleted).count();
        int completionRate = totalTasks > 0 ? (int) ((completedTasks * 100) / totalTasks) : 0;

        // 2. 专注统计
        List<AppFocusEntity> weekFocus = focusService.listByCreatedTime(userId, weekStart, weekEnd);
        int focusMinutes = weekFocus.stream()
            .mapToInt(focus -> ObjectUtil.defaultIfNull(focus.getDurationMinutes(), 0)).sum();
        int focusSessions = weekFocus.size();

        // 3. 习惯打卡统计
        List<AppHabitRecordEntity> weekHabitRecords = habitService.listRecordsByCreatedTime(userId, weekStart, weekEnd);

        // 4. 最佳习惯（打卡次数最多的）
        String bestHabit = "无";
        if (!weekHabitRecords.isEmpty()) {
            Map<Integer, Long> habitCountMap = weekHabitRecords.stream()
                .collect(Collectors.groupingBy(AppHabitRecordEntity::getHabitId, Collectors.counting()));
            Integer bestHabitId = habitCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
            if (bestHabitId != null) {
                AppHabitEntity habit = habitService.getById(bestHabitId);
                if (habit != null) {
                    bestHabit = habit.getName();
                }
            }
        }

        // 5. 最佳日（完成任务最多的周几）
        int[] dailyCounts = new int[7];
        for (AppTaskEntity task : weekTasks) {
            if (task.getCompleted() && task.getDueDate() != null) {
                int dow = DateUtil.dayOfWeek(task.getDueDate()) - 1;
                if (dow >= 0 && dow < 7) {
                    dailyCounts[dow]++;
                }
            }
        }
        int bestDayIndex = 0;
        for (int i = 1; i < 7; i++) {
            if (dailyCounts[i] > dailyCounts[bestDayIndex]) {
                bestDayIndex = i;
            }
        }
        String bestDay = dailyCounts[bestDayIndex] > 0 ? DAY_NAMES[bestDayIndex] : "无";

        // 6. 对比上周
        Date lastWeekStart = DateUtil.offsetWeek(weekStart, -1);
        Date lastWeekEnd = DateUtil.offsetDay(weekStart, -1);
        List<AppTaskEntity> lastWeekTasks = taskService.listByDueDateRange(userId, lastWeekStart, lastWeekEnd);
        int lastTotal = lastWeekTasks.size();
        long lastCompleted = lastWeekTasks.stream().filter(AppTaskEntity::getCompleted).count();
        int lastRate = lastTotal > 0 ? (int) ((lastCompleted * 100) / lastTotal) : 0;
        int diff = completionRate - lastRate;
        String comparedLastWeek = (diff >= 0 ? "+" : "") + diff + "%";

        Map<String, Object> result = new HashMap<>();
        result.put("weekRange", weekRange);
        result.put("totalTasks", totalTasks);
        result.put("completedTasks", (int) completedTasks);
        result.put("completionRate", completionRate);
        result.put("focusMinutes", focusMinutes);
        result.put("focusSessions", focusSessions);
        result.put("habitCheckins", weekHabitRecords.size());
        result.put("bestHabit", bestHabit);
        result.put("bestDay", bestDay);
        result.put("streakDays", calculateStreakDays(userId, weekStart));
        result.put("comparedLastWeek", comparedLastWeek);
        return result;
    }

    /**
     * 计算最长连续活跃天数
     */
    private int calculateMaxStreak(Map<String, Integer> dailyMap) {
        List<String> sortedDays = dailyMap.keySet().stream().sorted().collect(Collectors.toList());
        int maxStreak = 0;
        int currentStreak = 0;
        Date prev = null;
        for (String day : sortedDays) {
            Date curr = DateUtil.parse(day);
            long diffDays = prev == null ? 0 : (curr.getTime() - prev.getTime()) / (1000 * 60 * 60 * 24);
            currentStreak = prev != null && diffDays == 1 ? currentStreak + 1 : 1;
            maxStreak = Math.max(maxStreak, currentStreak);
            prev = curr;
        }
        return maxStreak;
    }

    /**
     * 从指定日期起向前统计连续打卡天数，最多回溯 365 天
     */
    private int calculateStreakDays(Integer userId, Date startDate) {
        int streak = 0;
        Date currentDate = startDate;
        while (streak < 365) {
            Date dayStart = DateUtil.beginOfDay(currentDate);
            Date dayEnd = DateUtil.endOfDay(currentDate);
            boolean active = focusService.countByCreatedTime(userId, dayStart, dayEnd) > 0
                || taskService.countCompletedByCreatedTimeRange(userId, dayStart, dayEnd) > 0;
            if (!active) {
                break;
            }
            streak++;
            currentDate = DateUtil.offsetDay(currentDate, -1);
        }
        return streak;
    }
}
