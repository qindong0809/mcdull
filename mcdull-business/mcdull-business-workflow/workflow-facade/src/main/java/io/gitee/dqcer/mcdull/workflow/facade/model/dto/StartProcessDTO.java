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
 * 发起流程请求。
 * <p>
 * 业务展示字段由调用方以 attributes 传入，工作流不解释其含义，原样落到引擎扩展字段，
 * 查询时再通过 attributes 原样返回。
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

    /**
     * 指定首节点办理人。未传时按流程定义中的 handlerPath 解析，仍无法解析则落给发起人。
     */
    private List<String> assigneeIds = new ArrayList<>();

    /**
     * 业务属性快照，供业务方展示使用，工作流不解释其 key 的含义。
     * <p>
     * 典型内容：businessTitle、businessNo、applicantName、departmentName、amountText 等。
     * 工作流只负责原样落到引擎扩展字段，并在查询接口中通过 attributes 原样返回。
     * 不要放敏感信息、大对象、附件内容或流程判断依赖的数据。
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 流程变量，供引擎做条件判断和流转控制，不是业务展示快照。
     * <p>
     * 典型内容：leaveDays、amount、riskLevel、expenseType、needFinanceReview 等。
     * 例如 leaveDays &gt; 5 走总监审批。只放基本类型、字符串及其集合，不放权限规则或大对象。
     */
    private Map<String, Object> variables = new HashMap<>();
}
