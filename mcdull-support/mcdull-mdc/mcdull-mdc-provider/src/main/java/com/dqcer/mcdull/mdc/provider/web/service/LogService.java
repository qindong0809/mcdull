package com.dqcer.mcdull.mdc.provider.web.service;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.SysLogDTO;
import com.dqcer.mcdull.mdc.api.entity.SysLogEntity;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogService {

    @Resource
    private ILogRepository logRepository;

    public Result<Integer> batchSave(List<SysLogDTO> dto) {
        List<SysLogEntity> entities = Lists.newArrayList();
        for (SysLogDTO sysLogDTO : dto) {
            entities.add(sysLogDTO);
        }
        logRepository.saveBatch(entities, entities.size());
        return Result.ok(entities.size());
    }
}
