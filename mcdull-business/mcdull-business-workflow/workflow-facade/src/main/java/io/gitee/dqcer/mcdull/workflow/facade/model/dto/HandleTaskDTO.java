package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 办理任务请求。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HandleTaskDTO extends BaseWorkflowRequest {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "taskId 不能为空")
    private Long taskId;

    @NotBlank(message = "action 不能为空")
    private String action;

    private String comment;

    /**
     * 流程变量，供引擎做条件判断和流转控制，不是业务展示快照。
     * <p>
     * 典型内容：amount、riskLevel、needFinanceReview 等；只放基本类型、字符串及其集合。
     */
    private Map<String, Object> variables = new HashMap<>();

    private List<String> targetAssigneeIds = new ArrayList<>();

    /**
     * 业务属性快照，供业务方展示使用，工作流不解释其 key 的含义。
     * <p>
     * 典型内容：businessTitle、approverName、commentText 等；工作流只原样保存并回传。
     */
    private Map<String, Object> attributes = new HashMap<>();
}
