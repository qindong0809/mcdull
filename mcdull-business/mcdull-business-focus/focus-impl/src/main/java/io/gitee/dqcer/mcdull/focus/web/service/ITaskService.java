package io.gitee.dqcer.mcdull.focus.web.service;

import io.gitee.dqcer.mcdull.focus.model.dto.TaskCreateDTO;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskUpdateDTO;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;

import java.util.Date;
import java.util.List;

/**
 * 任务服务
 *
 * @author dqcer
 */
public interface ITaskService {

    /**
     * 查询用户的任务列表
     *
     * @param userId    用户 ID
     * @param completed 完成状态，为空表示不限制
     * @return 任务列表
     */
    List<AppTaskEntity> listByUser(Integer userId, Boolean completed);

    /**
     * 按批量建单策略创建任务，游客账号拒绝写入
     *
     * @param dto    创建参数
     * @param userId 用户 ID
     * @return 创建成功的任务
     */
    List<AppTaskEntity> createBatch(TaskCreateDTO dto, Integer userId);

    /**
     * 更新任务
     *
     * @param dto 更新参数
     */
    void update(TaskUpdateDTO dto);

    /**
     * 删除任务
     *
     * @param id 任务 ID
     */
    void deleteById(Integer id);

    /**
     * 反转任务完成状态
     *
     * @param id 任务 ID
     */
    void toggleCompleted(Integer id);

    /**
     * 查询用户在到期时间区间内的任务
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 任务列表
     */
    List<AppTaskEntity> listByDueDateRange(Integer userId, Date startTime, Date endTime);

    /**
     * 统计用户在到期时间区间内已完成的任务数
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 已完成任务数
     */
    long countCompletedByDueDateRange(Integer userId, Date startTime, Date endTime);

    /**
     * 查询用户在更新时间区间内已完成的任务
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 已完成任务列表
     */
    List<AppTaskEntity> listCompletedByUpdatedTimeRange(Integer userId, Date startTime, Date endTime);

    /**
     * 统计用户在创建时间区间内已完成的任务数
     *
     * @param userId    用户 ID
     * @param startTime 区间开始
     * @param endTime   区间结束
     * @return 已完成任务数
     */
    long countCompletedByCreatedTimeRange(Integer userId, Date startTime, Date endTime);
}
