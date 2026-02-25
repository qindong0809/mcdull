package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.SerialNumberRecordMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Serial Number Record Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SerialNumberRecordServiceImpl
        extends BasicCurdServiceImpl<SerialNumberRecordMapper, SerialNumberRecordEntity> implements ISerialNumberRecordService {


    @Override
    public PagedVO<SerialNumberRecordEntity> query(SerialNumberRecordQueryDTO dto) {
        Page<SerialNumberRecordEntity> entityPage = this.selectPage(dto);
        return PageUtil.toPage(entityPage);
    }

    @Override
    public List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId) {
        LambdaQueryWrapper<SerialNumberRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(SerialNumberRecordEntity::getSerialNumberId, serialNumberId);
        query.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectList(query);
    }

    @Override
    public void batchSave(SerialNumberRecordEntity oldRecord, List<Integer> resultList) {
        List<SerialNumberRecordEntity> list = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            SerialNumberRecordEntity entity = new SerialNumberRecordEntity();
            entity.setSerialNumberId(oldRecord.getSerialNumberId());
            entity.setLastNumber(resultList.get(i));
            entity.setLastTime(new Date());
            entity.setRecordDate(new Date());
            entity.setCount(oldRecord.getCount() + i + 1);
            list.add(entity);
        }
        this.saveBatch(list, list.size());
    }

    @Override
    public void batchSave(SerialNumberEntity configEntity, List<Integer> resultList) {
        List<SerialNumberRecordEntity> list = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            SerialNumberRecordEntity entity = new SerialNumberRecordEntity();
            entity.setSerialNumberId(configEntity.getId());
            entity.setLastNumber(resultList.get(i));
            entity.setLastTime(new Date());
            entity.setRecordDate(new Date());
            entity.setCount(i + 1);
            list.add(entity);
        }
        this.saveBatch(list, list.size());
    }


    public List<SerialNumberRecordEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<SerialNumberRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SerialNumberRecordEntity::getId, idList);
        List<SerialNumberRecordEntity> list =  baseMapper.selectList(wrapper);
        if (ObjectUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    public Page<SerialNumberRecordEntity> selectPage(SerialNumberRecordQueryDTO param) {
        LambdaQueryWrapper<SerialNumberRecordEntity> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    public SerialNumberRecordEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    public void insert(SerialNumberRecordEntity entity) {
        baseMapper.insert(entity);
    }

    public boolean exist(SerialNumberRecordEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}
