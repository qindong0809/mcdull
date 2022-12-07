package com.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.mdc.api.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.LogEntity;

public interface ILogRepository extends IService<LogEntity> {

    Page<LogEntity> selectPage(LogLiteDTO dto);
}
