package com.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.provider.model.entity.LogDO;

public interface ILogRepository extends IService<LogDO> {

    Page<LogDO> selectPage(LogLiteDTO dto);
}
