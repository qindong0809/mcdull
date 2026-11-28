package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量审批状态查询。
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
public class ApprovalStatusQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程编码，对应 warm-flow 的 flowCode。
     */
    @NotBlank(message = "code 不能为空")
    private String code;

    @NotEmpty(message = "businessIds 不能为空")
    private List<String> businessIds = new ArrayList<>();
}
