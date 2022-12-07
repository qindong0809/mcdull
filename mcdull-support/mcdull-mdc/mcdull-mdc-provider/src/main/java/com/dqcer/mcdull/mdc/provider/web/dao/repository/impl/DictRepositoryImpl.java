package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.utils.StrUtil;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.DictEntity;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.DictMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

@Service
public class DictRepositoryImpl extends ServiceImpl<DictMapper, DictEntity> implements IDictRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link DictEntity}>
     */
    @Override
    public Page<DictEntity> selectPage(DictLiteDTO dto) {
        LambdaQueryWrapper<DictEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictEntity::getCode, keyword).or().like(DictEntity::getSelectType, keyword).or().like(DictEntity::getName, keyword));
        }
        query.orderByAsc(DictEntity::getSelectType, DictEntity::getSort);
        return baseMapper.selectPage(new Page<>(dto.getCurrPage(), dto.getPageSize()), query);
    }

}
