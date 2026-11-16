package io.gitee.dqcer.mcdull.focus.web.service;

import io.gitee.dqcer.mcdull.focus.model.entity.AppScheduleEntity;

import java.util.Date;
import java.util.List;

/**
 * 日程服务
 *
 * @author dqcer
 */
public interface IScheduleService {

    /**
     * 查询用户在时间区间内的日程
     *
     * @param userId    用户 ID
     * @param startTime 区间开始，为空表示不限制
     * @param endTime   区间结束，为空表示不限制
     * @return 日程列表
     */
    List<AppScheduleEntity> listByUser(Integer userId, Date startTime, Date endTime);

    /**
     * 新增日程
     *
     * @param entity 日程
     */
    void save(AppScheduleEntity entity);

    /**
     * 更新日程
     *
     * @param entity 日程
     */
    void updateById(AppScheduleEntity entity);

    /**
     * 删除自己创建的日程
     *
     * @param id     日程 ID
     * @param userId 用户 ID
     */
    void deleteOwnById(Integer id, Integer userId);
}
