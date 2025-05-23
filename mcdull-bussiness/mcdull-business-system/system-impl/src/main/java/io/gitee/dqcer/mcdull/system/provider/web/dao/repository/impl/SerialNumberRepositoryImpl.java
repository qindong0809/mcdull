package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.SerialNumberMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.ISerialNumberRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Serial Number Repository Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SerialNumberRepositoryImpl
        extends CrudRepository<SerialNumberMapper, SerialNumberEntity> implements ISerialNumberRepository {

    @Override
    public List<SerialNumberEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<SerialNumberEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SerialNumberEntity::getId, idList);
        List<SerialNumberEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public Page<SerialNumberEntity> selectPage(ChangeLogQueryDTO param) {
        LambdaQueryWrapper<SerialNumberEntity> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    @Override
    public SerialNumberEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    public void insert(SerialNumberEntity entity) {
        baseMapper.insert(entity);
    }

    @Override
    public boolean exist(SerialNumberEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}