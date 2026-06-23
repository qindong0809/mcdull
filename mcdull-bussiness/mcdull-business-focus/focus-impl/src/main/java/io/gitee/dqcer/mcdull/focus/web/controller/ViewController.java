package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppFocusMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitRecordMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppTaskMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * 4. 获取统计概览
     * POST /app/stats/overview
     *
     * 请求参数：
     *
     * {
     *   "period": "week"             // string, "week" 或 "month"
     * }
     * 响应 data：
     *
     *
     * {
     *   "completionRate": 75,         // int, 完成率百分比 0-100
     *   "focusMinutes": 320,          // int, 本期专注总时长（分钟）
     *   "lastPeriodFocusMinutes": 280,// int, 上期专注总时长（用于对比）
     *   "streakDays": 12,             // int, 连续打卡天数
     *   "dailyCompletions": [3,4,2,5,4,3,5]  // int[], 每日完成任务数（周模式7个，月模式30/31个）
     * }
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
