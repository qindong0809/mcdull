package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 办理任务请求。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HandleTaskDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "taskId 不能为空")
    private Long taskId;

    @NotBlank(message = "action 不能为空")
    private String action;

    private String comment;

    private Map<String, Object> variables = new HashMap<>();

    private List<String> targetAssigneeIds = new ArrayList<>();

    private Map<String, String> targetAssigneeNames = new HashMap<>();

    /**
     * 乐观锁版本，null 表示不校验。
     */
    private Long expectedRevision;

    @NotBlank(message = "idempotencyKey 不能为空")
    private String idempotencyKey;
}
