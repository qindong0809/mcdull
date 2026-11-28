package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例轻量状态 VO。业务列表页批量渲染首选该结构。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long instanceId;

    private String flowCode;

    private String businessId;

    /**
     * 业务属性快照，由调用方按 key 解释，工作流只原样回传。
     * <p>
     * 典型内容：businessTitle、businessNo、applicantName、departmentName、amountText 等。
     */
    private Map<String, Object> attributes = new HashMap<>();

    private String flowStatus;

    private String statusText;

    private String currentNodeCode;

    private String currentNodeName;

    @Builder.Default
    private List<String> currentApproverIds = new ArrayList<>();

    private String updateTime;
}
