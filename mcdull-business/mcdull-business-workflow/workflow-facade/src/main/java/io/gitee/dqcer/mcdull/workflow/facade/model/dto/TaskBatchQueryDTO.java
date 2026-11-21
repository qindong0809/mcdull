package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 按任务 ID 批量查询。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class TaskBatchQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    @NotEmpty(message = "taskIds 不能为空")
    private List<Long> taskIds = new ArrayList<>();
}
