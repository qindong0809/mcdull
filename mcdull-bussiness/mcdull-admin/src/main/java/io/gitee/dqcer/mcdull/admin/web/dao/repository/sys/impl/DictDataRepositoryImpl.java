package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDataEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.DictDataMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictDataRepository;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 字典类型 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class DictDataRepositoryImpl extends ServiceImpl<DictDataMapper, DictDataEntity>  implements IDictDataRepository {

    @Override
    public List<DictDataEntity> dictType(String dictType) {
        LambdaQueryWrapper<DictDataEntity> wrapper = Wrappers.lambdaQuery(DictDataEntity.class);
        wrapper.eq(DictDataEntity::getStatus, StatusEnum.ENABLE.getCode());
        wrapper.eq(DictDataEntity::getDictType, dictType);
        wrapper.orderByAsc(DictDataEntity::getDictSort);
        List<DictDataEntity> list = baseMapper.selectList(wrapper);

        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public Page<DictDataEntity> selectPage(DictDataLiteDTO dto) {
        LambdaQueryWrapper<DictDataEntity> lambda = new QueryWrapper<DictDataEntity>().lambda();
        lambda.like(DictDataEntity::getDictType, dto.getDictType());
        String dictLabel = dto.getDictLabel();
        if (StrUtil.isNotBlank(dictLabel)) {
            lambda.like(DictDataEntity::getDictLabel, dictLabel);
        }
        String status = dto.getStatus();
        if (ObjUtil.isNotNull(status)) {
            lambda.eq(DictDataEntity::getStatus, status);
        }
        lambda.orderByAsc(DictDataEntity::getDictSort);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<DictDataEntity> getNameList(String dictType, String dictValue) {
        LambdaQueryWrapper<DictDataEntity> lambda = new QueryWrapper<DictDataEntity>().lambda();
        lambda.eq(DictDataEntity::getDictType, dictType);
        lambda.eq(DictDataEntity::getDictValue, dictValue);
        return baseMapper.selectList(lambda);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }
}