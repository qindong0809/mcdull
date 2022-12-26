package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.provider.model.entity.LogDO;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.LogMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import org.springframework.stereotype.Service;

/**
 * 日志 数据库操作封装实现层
 *
 * @author dqcer
 * @version 2022/12/26
 */
@Service
public class LogRepositoryImpl extends ServiceImpl<LogMapper, LogDO> implements ILogRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link LogDO}>
     */
    @Override
    public Page<LogDO> selectPage(LogLiteDTO dto) {
        LambdaQueryWrapper<LogDO> query = Wrappers.lambdaQuery();
        query.orderByDesc(LogDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }
}
