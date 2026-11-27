package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批流程图视图。
 *
 * <p>businessId 为空时只返回定义图；不为空时附带当前节点、已走节点与审批历史。</p>
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDiagramVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String definitionKey;

    @Builder.Default
    private List<DiagramVO.DiagramNodeVO> nodes = new ArrayList<>();

    @Builder.Default
    private List<DiagramVO.DiagramEdgeVO> edges = new ArrayList<>();

    private Long instanceId;

    private String businessId;

    private String flowStatus;

    private String statusText;

    private String currentNodeCode;

    private String currentNodeName;

    @Builder.Default
    private List<String> currentApproverIds = new ArrayList<>();

    @Builder.Default
    private List<String> currentApproverNames = new ArrayList<>();

    @Builder.Default
    private List<String> visitedNodeCodes = new ArrayList<>();

    @Builder.Default
    private List<HistoryVO> history = new ArrayList<>();
}
