package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.util.StrUtil;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.DictDO;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.DictMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

@Service
public class DictRepositoryImpl extends ServiceImpl<DictMapper, DictDO> implements IDictRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link DictDO}>
     */
    @Override
    public Page<DictDO> selectPage(DictLiteDTO dto) {
        LambdaQueryWrapper<DictDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictDO::getCode, keyword).or().like(DictDO::getSelectType, keyword).or().like(DictDO::getName, keyword));
        }
        query.orderByAsc(DictDO::getSelectType, DictDO::getSort);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }

}
