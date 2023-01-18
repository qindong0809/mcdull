package io.gitee.dqcer.mcdull.framework.mysql.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认数据变化记录
 *
 * @author dqcer
 * @date 2023/01/18 22:01:70
 */
public class DefaultDataChangeRecorder implements IDataChangeRecorder {

    private static final Logger log = LoggerFactory.getLogger(DefaultDataChangeRecorder.class);

    /**
     * 打印
     *
     * @param operationResult 操作结果
     */
    @Override
    public void dataInnerInterceptor(DataChangeRecorderInnerInterceptor.OperationResult operationResult) {
        log.info("{}", operationResult);
    }
}
