package io.gitee.dqcer.mcdull.focus.web.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskCreateDTO;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskUpdateDTO;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;
import io.gitee.dqcer.mcdull.focus.model.enums.TaskBatchBuildDateEnum;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppTaskMapper;
import io.gitee.dqcer.mcdull.focus.web.service.ITaskService;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务服务实现
 *
 * @author dqcer
 */
@Service
public class TaskServiceImpl implements ITaskService {

    /**
     * 批量建单时一天内分布的截止时间
     */
    private static final List<String> BATCH_DUE_TIMES =
        ListUtil.of("09:45", "11:00", "14:00", "15:15", "16:00", "17:15");

    @Resource
    private AppTaskMapper appTaskMapper;

    @Override
    public List<AppTaskEntity> listByUser(Integer userId, Boolean completed) {
        LambdaQueryWrapper<AppTaskEntity> query = Wrappers.lambdaQuery();
        query.eq(ObjectUtil.isNotNull(completed), AppTaskEntity::getCompleted, completed);
        query.eq(AppTaskEntity::getCreatedBy, userId);
        query.orderByAsc(AppTaskEntity::getDueDate);
        return appTaskMapper.selectList(query);
    }

    @Override
    public List<AppTaskEntity> createBatch(TaskCreateDTO dto, Integer userId) {
        validReadOnlyUser();
        List<AppTaskEntity> created = new ArrayList<>();
        for (Date date : buildDateList(dto.getBatchBuildDate(), dto.getDueDate())) {
            AppTaskEntity entity = new AppTaskEntity();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setDueDate(date);
            entity.setPriority(dto.getPriority());
            entity.setCategory(dto.getCategory());
            entity.setCompleted(false);
            entity.setReminderEnabled(dto.getReminderEnabled());
            entity.setCreatedBy(userId);
            appTaskMapper.insert(entity);
            created.add(entity);
        }
        return created;
    }

    @Override
    public void update(TaskUpdateDTO dto) {
        validReadOnlyUser();
        AppTaskEntity update = new AppTaskEntity();
        update.setId(dto.getId());
        update.setName(dto.getName());
        update.setDescription(dto.getDescription());
        update.setDueDate(dto.getDueDate());
        update.setPriority(dto.getPriority());
        update.setCategory(dto.getCategory());
        update.setReminderEnabled(dto.getReminderEnabled());
        appTaskMapper.updateById(update);
    }

    @Override
    public void deleteById(Integer id) {
        validReadOnlyUser();
        appTaskMapper.deleteById(id);
    }

    @Override
    public void toggleCompleted(Integer id) {
        validReadOnlyUser();
        AppTaskEntity task = appTaskMapper.selectById(id);
        task.setCompleted(!task.getCompleted());
        appTaskMapper.updateById(task);
    }

    @Override
    public List<AppTaskEntity> listByDueDateRange(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppTaskEntity> query = Wrappers.lambdaQuery();
        query.eq(AppTaskEntity::getCreatedBy, userId);
        query.between(AppTaskEntity::getDueDate, startTime, endTime);
        return appTaskMapper.selectList(query);
    }

    @Override
    public long countCompletedByDueDateRange(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppTaskEntity> query = Wrappers.lambdaQuery();
        query.eq(AppTaskEntity::getCreatedBy, userId);
        query.between(AppTaskEntity::getDueDate, startTime, endTime);
        query.eq(AppTaskEntity::getCompleted, true);
        return appTaskMapper.selectCount(query);
    }

    @Override
    public List<AppTaskEntity> listCompletedByUpdatedTimeRange(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppTaskEntity> query = Wrappers.lambdaQuery();
        query.eq(AppTaskEntity::getCreatedBy, userId);
        query.eq(AppTaskEntity::getCompleted, true);
        query.between(AppTaskEntity::getUpdatedTime, startTime, endTime);
        return appTaskMapper.selectList(query);
    }

    @Override
    public long countCompletedByCreatedTimeRange(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppTaskEntity> query = Wrappers.lambdaQuery();
        query.eq(AppTaskEntity::getCreatedBy, userId);
        query.between(AppTaskEntity::getCreatedTime, startTime, endTime);
        query.eq(AppTaskEntity::getCompleted, true);
        return appTaskMapper.selectCount(query);
    }

    /**
     * 游客模式下不能操作，赶快取注册一个账号吧
     */
    private void validReadOnlyUser() {
        Integer userId = UserContextHolder.userId();
        if (userId == 1) {
            throw new BusinessException(CodeEnum.READ_ONLY);
        }
    }

    private List<Date> buildDateList(Integer batchBuildDate, Date dueDate) {
        List<Date> dateList = new ArrayList<>();
        if (ObjectUtil.equals(batchBuildDate, TaskBatchBuildDateEnum.TODAY.getCode())) {
            for (String time : BATCH_DUE_TIMES) {
                dateList.add(DateUtil.parse(DateUtil.today() + " " + time));
            }
        } else if (ObjectUtil.equals(batchBuildDate, TaskBatchBuildDateEnum.WEEK.getCode())) {
            // 获取本周的开始日期
            Date beginDate = DateUtil.beginOfWeek(new Date());
            for (int i = 0; i < 7; i++) {
                for (String time : BATCH_DUE_TIMES) {
                    dateList.add(DateUtil.parse(DateUtil.formatDate(DateUtil.offsetDay(beginDate, i)) + " " + time));
                }
            }
        } else {
            dateList.add(dueDate);
        }
        return dateList;
    }
}
