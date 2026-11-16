package io.gitee.dqcer.mcdull.focus.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.entity.AppScheduleEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppScheduleMapper;
import io.gitee.dqcer.mcdull.focus.web.service.IScheduleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 日程服务实现
 *
 * @author dqcer
 */
@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Resource
    private AppScheduleMapper appScheduleMapper;

    @Override
    public List<AppScheduleEntity> listByUser(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppScheduleEntity> query = Wrappers.lambdaQuery();
        // 当日，本周，本月进行过滤筛选
        if (ObjectUtil.isNotNull(startTime)) {
            query.ge(AppScheduleEntity::getStartTime, DateUtil.beginOfDay(startTime));
        }
        if (ObjectUtil.isNotNull(endTime)) {
            query.le(AppScheduleEntity::getEndTime, DateUtil.endOfDay(endTime));
        }
        query.eq(AppScheduleEntity::getCreatedBy, userId);
        query.orderByAsc(AppScheduleEntity::getCreatedBy);
        return appScheduleMapper.selectList(query);
    }

    @Override
    public void save(AppScheduleEntity entity) {
        appScheduleMapper.insert(entity);
    }

    @Override
    public void updateById(AppScheduleEntity entity) {
        appScheduleMapper.updateById(entity);
    }

    @Override
    public void deleteOwnById(Integer id, Integer userId) {
        // 校验归属权限：只能删除自己的日程
        LambdaQueryWrapper<AppScheduleEntity> query = Wrappers.lambdaQuery();
        query.eq(AppScheduleEntity::getId, id);
        query.eq(AppScheduleEntity::getCreatedBy, userId);
        appScheduleMapper.delete(query);
    }
}
