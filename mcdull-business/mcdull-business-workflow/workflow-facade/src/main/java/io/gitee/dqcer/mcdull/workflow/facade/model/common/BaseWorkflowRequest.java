package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务间请求公共字段。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class BaseWorkflowRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作人 ID。写操作必填。
     */
    private String operatorId;

    /**
     * 操作人名称快照。
     */
    private String operatorName;

    /**
     * 调用方系统标识。
     */
    private String sourceSystem;

    /**
     * 链路追踪 ID，可由 Feign 透传。
     */
    private String traceId;
}
