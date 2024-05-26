package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.ChangeLogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IChangeLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
@Service
public class ChangeLogRepositoryImpl
        extends ServiceImpl<ChangeLogMapper, ChangeLogEntity>  implements IChangeLogRepository {

    private static final Logger log = LoggerFactory.getLogger(ChangeLogRepositoryImpl.class);

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
        LambdaQueryWrapper<ChangeLogEntity> lambda = new QueryWrapper<ChangeLogEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
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
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    @Override
    public boolean exist(ChangeLogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}