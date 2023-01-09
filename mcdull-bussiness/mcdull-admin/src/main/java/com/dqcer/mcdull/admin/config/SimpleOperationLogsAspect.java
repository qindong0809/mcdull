package com.dqcer.mcdull.admin.config;

import com.dqcer.mcdull.framework.web.aspect.OperationLogsAspect;
import com.dqcer.mcdull.framework.web.feign.model.LogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleOperationLogsAspect extends OperationLogsAspect {

    private static final Logger log = LoggerFactory.getLogger(SimpleOperationLogsAspect.class);

    @Override
    protected void saveLog(LogDTO entity) {
        log.info("log: {}", entity);
    }
}
