package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.constraints.NotBlank;
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

    private Map<String, Object> variables = new HashMap<>();

    /**
     * 乐观锁版本，null 表示不校验。
     */
    private Long expectedRevision;

    @NotBlank(message = "idempotencyKey 不能为空")
    private String idempotencyKey;
}
