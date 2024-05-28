package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.SerialNumberRecordMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISerialNumberRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
@Service
public class SerialNumberRecordRepositoryImpl
        extends ServiceImpl<SerialNumberRecordMapper, SerialNumberRecordEntity>  implements ISerialNumberRecordRepository {

    private static final Logger log = LoggerFactory.getLogger(SerialNumberRecordRepositoryImpl.class);

    @Override
    public List<SerialNumberRecordEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<SerialNumberRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SerialNumberRecordEntity::getId, idList);
        List<SerialNumberRecordEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public Page<SerialNumberRecordEntity> selectPage(SerialNumberRecordQueryDTO param) {
        LambdaQueryWrapper<SerialNumberRecordEntity> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    @Override
    public SerialNumberRecordEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    public void insert(SerialNumberRecordEntity entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    @Override
    public boolean exist(SerialNumberRecordEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId) {
        LambdaQueryWrapper<SerialNumberRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(SerialNumberRecordEntity::getSerialNumberId, serialNumberId);
        query.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectList(query);
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