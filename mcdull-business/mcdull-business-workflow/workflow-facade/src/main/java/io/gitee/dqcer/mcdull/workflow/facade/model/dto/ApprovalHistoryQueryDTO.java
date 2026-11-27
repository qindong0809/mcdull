package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 审批历史查询请求。
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
public class ApprovalHistoryQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "tenantId 不能为空")
    private String tenantId;

    @NotBlank(message = "code 不能为空")
    private String code;

    @NotBlank(message = "businessId 不能为空")
    private String businessId;

    /**
     * 返回条数上限，null 时默认 50，最大 200。
     */
    private Integer limit;
}
