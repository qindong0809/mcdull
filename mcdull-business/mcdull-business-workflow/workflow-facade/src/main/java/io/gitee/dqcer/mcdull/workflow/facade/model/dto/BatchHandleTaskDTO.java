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
 * 批量办理请求。每条独立事务；幂等由业务服务控制。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BatchHandleTaskDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "batchId 不能为空")
    private String batchId;

    @Valid
    @NotEmpty(message = "items 不能为空")
    private List<HandleTaskDTO> items = new ArrayList<>();
}
