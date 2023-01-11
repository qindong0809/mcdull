package com.dqcer.mcdull.admin.web.service.sys.impl;

import com.dqcer.mcdull.admin.web.service.sys.ILogService;
import com.dqcer.mcdull.framework.mysql.config.DataChangeRecorderInnerInterceptor;
import com.dqcer.mcdull.framework.mysql.config.IDataChangeRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements ILogService, IDataChangeRecorder {

    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    public void dataInnerInterceptor(DataChangeRecorderInnerInterceptor.OperationResult operationResult) {
        log.info("{}", operationResult);
    }
}
