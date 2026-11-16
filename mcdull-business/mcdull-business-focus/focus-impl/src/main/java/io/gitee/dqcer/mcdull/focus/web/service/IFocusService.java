package io.gitee.dqcer.mcdull.focus.web.service;

import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;

import java.util.Date;
import java.util.List;

/**
 * 专注记录服务
 *
 * @author dqcer
 */
public interface IFocusService {

    /**
     * 保存专注记录
     *
     * @param entity 专注记录
     */
    void save(AppFocusEntity entity);

    /**
     * 按创建时间区间分页查询专注记录
     *
     * @param startTime 起始日期，为空表示不限制
     * @param endTime   结束日期，为空表示不限制
     * @param pageNo    页码
     * @param pageSize  每页条数
     * @return 当前页专注记录
     */
    List<AppFocusEntity> pageByCreatedTime(Date startTime, Date endTime, Integer pageNo, Integer pageSize);

    /**
     * 查询用户在时间区间内的专注记录
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 专注记录
     */
    List<AppFocusEntity> listByCreatedTime(Integer userId, Date startTime, Date endTime);

    /**
     * 统计用户在时间区间内的专注总时长
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 专注分钟数
     */
    int sumDurationMinutes(Integer userId, Date startTime, Date endTime);

    /**
     * 统计用户在时间区间内的专注次数
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 专注次数
     */
    long countByCreatedTime(Integer userId, Date startTime, Date endTime);
}
