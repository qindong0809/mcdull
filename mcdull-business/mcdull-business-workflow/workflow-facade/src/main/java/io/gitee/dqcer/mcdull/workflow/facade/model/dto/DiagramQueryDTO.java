package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程定义流程图查询。
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
public class DiagramQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    /**
     * 流程编码，对应 warm-flow 的 flowCode。
     */
    @NotBlank(message = "code 不能为空")
    private String code;
}
