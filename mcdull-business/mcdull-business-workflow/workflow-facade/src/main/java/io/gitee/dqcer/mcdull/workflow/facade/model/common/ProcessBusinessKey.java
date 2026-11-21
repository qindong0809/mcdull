package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工作流业务键。facade 中禁止用 Map 作为 Map 的 key，统一用该对象承载。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessBusinessKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "tenantId 不能为空")
    private String tenantId;

    @NotBlank(message = "flowCode 不能为空")
    private String flowCode;

    @NotBlank(message = "businessId 不能为空")
    private String businessId;
}
