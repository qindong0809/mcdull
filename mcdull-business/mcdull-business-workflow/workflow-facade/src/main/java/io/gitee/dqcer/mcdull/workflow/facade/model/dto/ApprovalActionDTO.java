package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 审批动作请求，用于同意/驳回场景。
 *
 * <p>动作由 API 语义决定（approve/reject），调用方不需要传 skipType 等引擎字段。</p>
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalActionDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "taskId 不能为空")
    private Long taskId;

    private String comment;

    /**
     * 流程变量，供引擎做条件判断和流转控制，不是业务展示快照。
     * <p>
     * 典型内容：amount、riskLevel、needFinanceReview 等；只放基本类型、字符串及其集合。
     */
    private Map<String, Object> variables = new HashMap<>();

    /**
     * 业务属性快照，供业务方展示使用，工作流不解释其 key 的含义。
     * <p>
     * 典型内容：approverName、approverDepartmentName、commentText 等；工作流只原样保存并回传。
     */
    private Map<String, Object> attributes = new HashMap<>();
}
