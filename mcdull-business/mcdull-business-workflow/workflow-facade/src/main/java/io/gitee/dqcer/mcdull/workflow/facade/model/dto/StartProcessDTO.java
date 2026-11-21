package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发起流程请求。所有业务展示字段均由调用方传入并落为工作流快照。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartProcessDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "flowCode 不能为空")
    private String flowCode;

    private String definitionId;

    @NotBlank(message = "businessId 不能为空")
    private String businessId;

    private String businessType;

    /**
     * 业务标题快照，工作流服务不回查业务表。
     */
    private String businessTitle;

    /**
     * 指定首节点办理人。未传时按流程定义中的 handlerPath 解析，仍无法解析则落给发起人。
     */
    private List<String> assigneeIds = new ArrayList<>();

    /**
     * userId -> userName 快照。
     */
    private Map<String, String> assigneeNames = new HashMap<>();

    /**
     * 流程变量，只保存基本类型、字符串及其集合。
     */
    private Map<String, Object> variables = new HashMap<>();

    @NotBlank(message = "idempotencyKey 不能为空")
    private String idempotencyKey;
}
