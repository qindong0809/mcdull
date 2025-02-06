package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.ChangeLogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IChangeLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Change  log repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class ChangeLogRepositoryImpl
        extends CrudRepository<ChangeLogMapper, ChangeLogEntity> implements IChangeLogRepository {

    @Override
    public List<ChangeLogEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<ChangeLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ChangeLogEntity::getId, idList);
        List<ChangeLogEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public Page<ChangeLogEntity> selectPage(ChangeLogQueryDTO param) {
        LambdaQueryWrapper<ChangeLogEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(i->i.like(ChangeLogEntity::getVersion, keyword)
                    .or().like(ChangeLogEntity::getContent, keyword));
        }
        Integer type = param.getType();
        if (ObjUtil.isNotNull(type)) {
            lambda.eq(ChangeLogEntity::getType, type);
        }
        LocalDate startDate = param.getPublicDateBegin();
        LocalDate endDate = param.getPublicDateEnd();
        if (ObjUtil.isAllNotEmpty(startDate, endDate)) {
            lambda.between(ChangeLogEntity::getPublicDate, startDate,
                    LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        LocalDate createTime = param.getCreateTime();
        if (ObjUtil.isNotNull(createTime)) {
            lambda.between(RelEntity::getCreatedTime, createTime, LocalDateTimeUtil.endOfDay(createTime.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    @Override
    public ChangeLogEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    public void insert(ChangeLogEntity entity) {
        baseMapper.insert(entity);
    }

    @Override
    public boolean exist(ChangeLogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}