package io.gitee.dqcer.mcdull.admin.framework.audit;

import io.gitee.dqcer.mcdull.framework.mongodb.MongoDBService;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.JsonUtil;
import io.gitee.dqcer.mcdull.framework.mysql.config.DataChangeRecorderInnerInterceptor;
import io.gitee.dqcer.mcdull.framework.mysql.config.IDataChangeRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 默认数据变化记录
 *
 * @author dqcer
 * @date 2023/01/18 22:01:70
 */
@Component
public class DefaultDataChangeRecorder implements IDataChangeRecorder {

    private static final Logger log = LoggerFactory.getLogger(DefaultDataChangeRecorder.class);

    @Resource
    private MongoDBService mongoDBService;

    /**
     * 打印
     *
     * @param operationResult 操作结果
     */
    @Override
    public void dataInnerInterceptor(DataChangeRecorderInnerInterceptor.OperationResult operationResult) {
        if (log.isDebugEnabled()) {
            log.debug("{}", operationResult);
        }
        mongoDBService.insertOrUpdate("data_change_record", build(operationResult));
    }

    public static TableAudit build(DataChangeRecorderInnerInterceptor.OperationResult operationResult) {
        TableAudit tableAudit = new TableAudit();
        tableAudit.setOperation(operationResult.getOperation());
        tableAudit.setRecordStatus(operationResult.isRecordStatus());
        tableAudit.setTableName(operationResult.getTableName());
        String changedData = operationResult.getChangedData();
        List<Map> keyValueVOS = JsonUtil.parseArray(changedData, Map.class);
        tableAudit.setFields(keyValueVOS);
        tableAudit.setCost(operationResult.getCost());
        tableAudit.setTraceId(UserContextHolder.getSession().getTraceId());
        return tableAudit;

    }
}
