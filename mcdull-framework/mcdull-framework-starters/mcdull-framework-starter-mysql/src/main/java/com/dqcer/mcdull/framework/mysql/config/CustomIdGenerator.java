package com.dqcer.mcdull.framework.mysql.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomIdGenerator implements IdentifierGenerator {

    private static final Logger log = LoggerFactory.getLogger(CustomIdGenerator.class);

    @Override
    public Long nextId(Object entity) {
        // FIXME: 2022/12/18 待优化
        IdWorker worker = new IdWorker(1,1,1);
        long id = worker.nextId();
        if (log.isDebugEnabled()) {
            log.debug("Custom Id Generator nextId: {}", id);
        }
        return id;
    }
}
