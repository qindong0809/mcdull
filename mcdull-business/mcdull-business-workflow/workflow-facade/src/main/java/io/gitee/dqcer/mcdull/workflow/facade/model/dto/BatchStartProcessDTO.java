package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量发起流程请求。每条独立事务、独立幂等键，单条失败不影响其他条目。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BatchStartProcessDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "batchId 不能为空")
    private String batchId;

    @Valid
    @NotEmpty(message = "items 不能为空")
    private List<StartProcessDTO> items = new ArrayList<>();
}
