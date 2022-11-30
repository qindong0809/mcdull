package com.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.mcdull.mdc.api.entity.SysLogEntity;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.LogMapper;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogRepositoryImpl extends ServiceImpl<LogMapper, SysLogEntity> implements ILogRepository {

}
