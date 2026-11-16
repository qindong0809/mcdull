package io.gitee.dqcer.mcdull.focus.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.focus.model.entity.AppFocusEntity;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppFocusMapper;
import io.gitee.dqcer.mcdull.focus.web.service.IFocusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 专注记录服务实现
 *
 * @author dqcer
 */
@Service
public class FocusServiceImpl implements IFocusService {

    @Resource
    private AppFocusMapper appFocusMapper;

    @Override
    public void save(AppFocusEntity entity) {
        appFocusMapper.insert(entity);
    }

    @Override
    public List<AppFocusEntity> pageByCreatedTime(Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<AppFocusEntity> query = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(startTime)) {
            query.ge(AppFocusEntity::getCreatedTime, DateUtil.beginOfDay(startTime));
        }
        if (ObjectUtil.isNotNull(endTime)) {
            query.le(AppFocusEntity::getCreatedTime, DateUtil.endOfDay(endTime));
        }
        Page<AppFocusEntity> page = appFocusMapper.selectPage(new Page<>(pageNo, pageSize), query);
        return page.getRecords();
    }

    @Override
    public List<AppFocusEntity> listByCreatedTime(Integer userId, Date startTime, Date endTime) {
        return appFocusMapper.selectList(createdTimeRangeQuery(userId, startTime, endTime));
    }

    @Override
    public int sumDurationMinutes(Integer userId, Date startTime, Date endTime) {
        return listByCreatedTime(userId, startTime, endTime).stream()
            .mapToInt(entity -> ObjectUtil.defaultIfNull(entity.getDurationMinutes(), 0))
            .sum();
    }

    @Override
    public long countByCreatedTime(Integer userId, Date startTime, Date endTime) {
        return appFocusMapper.selectCount(createdTimeRangeQuery(userId, startTime, endTime));
    }

    private LambdaQueryWrapper<AppFocusEntity> createdTimeRangeQuery(Integer userId, Date startTime, Date endTime) {
        LambdaQueryWrapper<AppFocusEntity> query = Wrappers.lambdaQuery();
        query.eq(AppFocusEntity::getCreatedBy, userId);
        query.between(AppFocusEntity::getCreatedTime, startTime, endTime);
        return query;
    }
}
