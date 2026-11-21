package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义版本批量查询，用于调用方本地缓存失效判断。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class DefinitionVersionBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    @NotEmpty(message = "definitionKeys 不能为空")
    private List<String> definitionKeys = new ArrayList<>();
}
