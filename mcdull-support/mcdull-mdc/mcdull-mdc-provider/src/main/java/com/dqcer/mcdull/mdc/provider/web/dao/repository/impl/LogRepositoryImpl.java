package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.mcdull.mdc.api.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.LogEntity;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.LogMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogRepositoryImpl extends ServiceImpl<LogMapper, LogEntity> implements ILogRepository {

    /**
     * @param dto
     * @return
     */
    @Override
    public Page<LogEntity> selectPage(LogLiteDTO dto) {
        LambdaQueryWrapper<LogEntity> query = Wrappers.lambdaQuery();
//        String keyword = dto.getKeyword();
//        if (StrUtil.isNotBlank(keyword)) {
//            query.and(i-> i.like(LogEntity::getPath, keyword));
//        }
        query.orderByDesc(LogEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrPage(), dto.getPageSize()), query);
    }
}
