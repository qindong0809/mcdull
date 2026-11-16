package io.gitee.dqcer.mcdull.focus.web.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitRecordEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitMapper;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppHabitRecordMapper;
import io.gitee.dqcer.mcdull.focus.web.service.IHabitService;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 习惯及打卡记录服务实现
 *
 * @author dqcer
 */
@Service
public class HabitServiceImpl implements IHabitService {

    @Resource
    private AppHabitMapper appHabitMapper;

    @Resource
    private AppHabitRecordMapper appHabitRecordMapper;

    @Override
    public void save(AppHabitEntity entity) {
        appHabitMapper.insert(entity);
    }

    @Override
    public void updateById(AppHabitEntity entity) {
        appHabitMapper.updateById(entity);
    }

    @Override
    public void deleteById(Integer id) {
        appHabitMapper.deleteById(id);
    }

    @Override
    public List<AppHabitEntity> listByUser(Integer userId) {
        LambdaQueryWrapper<AppHabitEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getCreatedBy, userId);
        return appHabitMapper.selectList(query);
    }

    @Override
    public Set<Integer> listHabitIdsCheckedToday(Integer userId) {
        LambdaQueryWrapper<AppHabitRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getCreatedBy, userId);
        Date now = new Date();
        return appHabitRecordMapper.selectList(query).stream()
            .filter(record -> DateUtil.isSameDay(record.getCreatedTime(), now))
            .map(AppHabitRecordEntity::getHabitId)
            .collect(Collectors.toSet());
    }

    @Override
    public void checkIn(Integer habitId) {
        AppHabitRecordEntity entity = new AppHabitRecordEntity();
        entity.setHabitId(habitId);
        appHabitRecordMapper.insert(entity);
    }

    @Override
    public void cancelTodayCheckIn(Integer habitId, Integer userId) {
        LambdaQueryWrapper<AppHabitRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(AppHabitRecordEntity::getHabitId, habitId);
        query.eq(BaseEntity::getCreatedBy, userId);
        // 只删除今天的打卡记录，不影响历史
        query.ge(AppHabitRecordEntity::getCreatedTime, DateUtil.beginOfDay(new Date()));
        query.le(AppHabitRecordEntity::getCreatedTime, DateUtil.endOfDay(new Date()));
        appHabitRecordMapper.delete(query);
    }

    @Override
    public int makeUpCheckIn(Integer habitId, Date date, Integer userId) {
        AppHabitRecordEntity entity = new AppHabitRecordEntity();
        entity.setHabitId(habitId);
        entity.setCreatedTime(date);
        appHabitRecordMapper.insert(entity);

        LambdaQueryWrapper<AppHabitRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(AppHabitRecordEntity::getHabitId, habitId);
        query.eq(BaseEntity::getCreatedBy, userId);
        return calculateMaxStreakDays(appHabitRecordMapper.selectList(query));
    }

    @Override
    public List<AppHabitRecordEntity> listRecordsByCreatedTime(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppHabitRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getCreatedBy, userId);
        query.between(AppHabitRecordEntity::getCreatedTime, startTime, endTime);
        return appHabitRecordMapper.selectList(query);
    }

    @Override
    public AppHabitEntity getById(Integer habitId) {
        return appHabitMapper.selectById(habitId);
    }

    /**
     * 按打卡日期从近到远计算最长连续打卡天数
     */
    private int calculateMaxStreakDays(List<AppHabitRecordEntity> records) {
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
            if (prev == null || DateUtil.between(day, prev, DateUnit.DAY) != 1) {
                current = 1;
            } else {
                current++;
            }
            max = Math.max(max, current);
            prev = day;
        }
        return max;
    }
}
