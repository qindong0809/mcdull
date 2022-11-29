package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.utils.StrUtil;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.SysDictEntity;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.DictMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

@Service
public class DictRepositoryImpl extends ServiceImpl<DictMapper, SysDictEntity> implements IDictRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link SysDictEntity}>
     */
    @Override
    public Page<SysDictEntity> selectPage(DictLiteDTO dto) {
        LambdaQueryWrapper<SysDictEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(SysDictEntity::getCode, keyword).or().like(SysDictEntity::getSelectType, keyword).or().like(SysDictEntity::getName, keyword));
        }
        query.orderByAsc(SysDictEntity::getSelectType, SysDictEntity::getSort);
        return baseMapper.selectPage(new Page<>(dto.getCurrPage(), dto.getPageSize()), query);
    }

}
