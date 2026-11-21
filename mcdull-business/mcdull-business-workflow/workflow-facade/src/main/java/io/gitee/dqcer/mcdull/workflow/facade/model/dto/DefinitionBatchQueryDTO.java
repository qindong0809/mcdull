package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义批量查询。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class DefinitionBatchQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    /**
     * 定义键，支持 def_ 前缀的数字 ID 或 flowCode。
     */
    @NotEmpty(message = "definitionKeys 不能为空")
    private List<String> definitionKeys = new ArrayList<>();
}
