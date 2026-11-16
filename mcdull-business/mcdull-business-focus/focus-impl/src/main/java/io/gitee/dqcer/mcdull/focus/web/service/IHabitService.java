package io.gitee.dqcer.mcdull.focus.web.service;

import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitEntity;
import io.gitee.dqcer.mcdull.focus.model.entity.AppHabitRecordEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 习惯及打卡记录服务
 *
 * @author dqcer
 */
public interface IHabitService {

    /**
     * 创建习惯
     *
     * @param entity 习惯
     */
    void save(AppHabitEntity entity);

    /**
     * 更新习惯
     *
     * @param entity 习惯
     */
    void updateById(AppHabitEntity entity);

    /**
     * 删除习惯
     *
     * @param id 习惯 ID
     */
    void deleteById(Integer id);

    /**
     * 查询用户的习惯列表
     *
     * @param userId 用户 ID
     * @return 习惯列表
     */
    List<AppHabitEntity> listByUser(Integer userId);

    /**
     * 查询用户今日已打卡的习惯 ID
     *
     * @param userId 用户 ID
     * @return 习惯 ID 集合
     */
    Set<Integer> listHabitIdsCheckedToday(Integer userId);

    /**
     * 习惯打卡
     *
     * @param habitId 习惯 ID
     */
    void checkIn(Integer habitId);

    /**
     * 取消今日打卡，不影响历史记录
     *
     * @param habitId 习惯 ID
     * @param userId  用户 ID
     */
    void cancelTodayCheckIn(Integer habitId, Integer userId);

    /**
     * 补签并返回最长连续打卡天数
     *
     * @param habitId 习惯 ID
     * @param date    补签日期
     * @param userId  用户 ID
     * @return 最长连续打卡天数
     */
    int makeUpCheckIn(Integer habitId, Date date, Integer userId);

    /**
     * 查询用户在时间区间内的打卡记录
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 打卡记录
     */
    List<AppHabitRecordEntity> listRecordsByCreatedTime(Integer userId, Date startTime, Date endTime);

    /**
     * 查询习惯
     *
     * @param habitId 习惯 ID
     * @return 习惯，不存在时返回空
     */
    AppHabitEntity getById(Integer habitId);
}
