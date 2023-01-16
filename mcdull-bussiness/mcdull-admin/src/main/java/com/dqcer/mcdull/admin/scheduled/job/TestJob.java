package io.gitee.dqcer.admin.scheduled.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * test job
 *
 * @author dqcer
 * @version 2022/12/29
 */
@Component
public class TestJob extends BaseJob {

    private static final Logger log = LoggerFactory.getLogger(TestJob.class);

    /**
     * execute job
     */
    @Override
    public void execute() {
        log.debug("定时任务");
    }
}
