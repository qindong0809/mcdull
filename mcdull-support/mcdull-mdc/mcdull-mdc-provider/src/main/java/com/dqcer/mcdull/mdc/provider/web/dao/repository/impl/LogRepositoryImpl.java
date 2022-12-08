package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.mcdull.mdc.api.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.LogDO;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.LogMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogRepositoryImpl extends ServiceImpl<LogMapper, LogDO> implements ILogRepository {

    /**
     * @param dto
     * @return
     */
    @Override
    public Page<LogDO> selectPage(LogLiteDTO dto) {
        LambdaQueryWrapper<LogDO> query = Wrappers.lambdaQuery();
//        String keyword = dto.getKeyword();
//        if (StrUtil.isNotBlank(keyword)) {
//            query.and(i-> i.like(LogDO::getPath, keyword));
//        }
        query.orderByDesc(LogDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }
}
