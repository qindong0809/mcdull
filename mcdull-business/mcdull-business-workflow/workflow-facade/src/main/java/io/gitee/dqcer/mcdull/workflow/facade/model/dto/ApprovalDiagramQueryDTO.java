package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 审批流程图查询请求。
 *
 * <p>businessId 为空表示绑定前预览流程定义图；
 * businessId 不为空表示查询审批中的实例进度图。</p>
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
public class ApprovalDiagramQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "tenantId 不能为空")
    private String tenantId;

    @NotBlank(message = "code 不能为空")
    private String code;

    private String businessId;
}
