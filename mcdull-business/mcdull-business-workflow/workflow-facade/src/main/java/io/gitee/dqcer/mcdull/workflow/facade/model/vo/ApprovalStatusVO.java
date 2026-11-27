package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批状态视图，面向业务系统展示“审批到哪一步”。
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    private Long instanceId;

    private String flowCode;

    private String businessId;

    private String businessTitle;

    private String flowStatus;

    private String statusText;

    private String currentNodeCode;

    private String currentNodeName;

    @Builder.Default
    private List<String> currentApproverIds = new ArrayList<>();

    @Builder.Default
    private List<String> currentApproverNames = new ArrayList<>();

    private String updateTime;
}
