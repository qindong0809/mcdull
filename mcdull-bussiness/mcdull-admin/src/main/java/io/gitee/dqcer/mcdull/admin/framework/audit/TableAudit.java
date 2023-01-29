package io.gitee.dqcer.mcdull.admin.framework.audit;

import io.gitee.dqcer.mcdull.framework.base.entity.DO;

import java.util.List;
import java.util.Map;

/**
 * 表审计
 *
 * @author dqcer
 * @since 2023/01/29
 */
public class TableAudit implements DO {

    private String operation;
    private boolean recordStatus;
    private String tableName;
    private List<Map> fields;
    private long cost;
    private String traceId;

    public String getOperation() {
        return operation;
    }

    public TableAudit setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public boolean isRecordStatus() {
        return recordStatus;
    }

    public TableAudit setRecordStatus(boolean recordStatus) {
        this.recordStatus = recordStatus;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public TableAudit setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
    public List<Map> getFields() {
        return fields;
    }

    public TableAudit setFields(List<Map> fields) {
        this.fields = fields;
        return this;
    }

    public long getCost() {
        return cost;
    }

    public TableAudit setCost(long cost) {
        this.cost = cost;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public TableAudit setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
