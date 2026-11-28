package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发起人撤回审批请求。
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CancelApprovalDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "code 不能为空")
    private String code;

    @NotBlank(message = "businessId 不能为空")
    private String businessId;

    /**
     * 撤回原因，可为空。
     */
    private String reason;
}
