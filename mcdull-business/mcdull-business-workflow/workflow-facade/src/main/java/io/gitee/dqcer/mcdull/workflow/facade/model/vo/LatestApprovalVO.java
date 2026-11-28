package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务单据最新审批结果。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestApprovalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long instanceId;

    private String flowCode;

    private String businessId;

    private String flowStatus;

    private String statusText;

    private String handlerId;

    /**
     * 业务属性快照，由调用方按 key 解释，工作流只原样回传。
     * <p>
     * 典型内容：approverName、approverDepartmentName、commentText 等。
     */
    private Map<String, Object> attributes = new HashMap<>();

    private String action;

    private String comment;

    private String updateTime;
}
