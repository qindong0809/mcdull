package io.gitee.dqcer.mcdull.framework.mysql.config;

/**
 * data变化记录
 *
 * @author dqcer
 * @since 2023/01/11 23:01:42
 */
public interface IDataChangeRecorder {

    /**
     * 打印
     *
     * @param operationResult 操作结果
     */
    void dataInnerInterceptor(DataChangeRecorderInnerInterceptor.OperationResult operationResult);
}
