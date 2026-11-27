package io.gitee.dqcer.mcdull.workflow.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalActionDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalDiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalHistoryQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalStatusQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.CancelApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCursorQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalDiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.HistoryVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.StartProcessResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskHandleResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 审批场景契约。
 *
 * <p>规则：只暴露审批语义；不暴露 nodeCode/skipType/handlerPath 等引擎概念；
 * 写操作必须带 idempotencyKey；租户来自 DTO 且与调用方上下文校验；
 * 批量读单批不超过 500，批量写单批不超过 100。</p>
 *
 * @author dqcer
 * @since 2026-11-27
 */
public interface WorkflowApprovalApiDef {

    /**
     * 场景：业务系统发起审批前按 flowCode 选择流程定义。
     * 规则：请求参数只有 code（flowCode），复用 defService.getByFlowCode；
     * 返回该 code 下所有版本定义，无结果返回空列表；
     * 不返回节点和连线，流程图走 batchGetDefinitionDiagrams。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/definitions/batch-get")
    InnerResult<List<DefinitionVO>> batchGetDefinitions(
            @Valid @RequestBody DefinitionQueryDTO dto);

    /**
     * 场景：绑定流程前按 flowCode 预览流程图。
     * 规则：请求参数只有 code（flowCode），复用 defService.getByFlowCode；
     * 返回该 code 下所有版本定义图，不含实例进度；无结果返回空列表。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/definitions/diagrams")
    InnerResult<List<DiagramVO>> batchGetDefinitionDiagrams(
            @Valid @RequestBody DiagramQueryDTO dto);

    /**
     * 场景：业务单据提交审批。
     * 规则：flowCode/businessId/operatorId 必填；businessId 在同一租户同一 flowCode 下只能对应一个运行中实例；
     * idempotencyKey 必填，重试必须复用同一个 key 且请求体完全一致；
     * 成功返回 instanceId/status/currentNode；失败返回 999631/999632/999634 等业务错误码。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/start")
    InnerResult<StartProcessResult> startApproval(@Valid @RequestBody StartApprovalDTO dto);

    /**
     * 场景：业务单据列表页按 flowCode 批量展示审批状态。
     * 规则：请求参数为 code（flowCode）+ businessIds；按 tenantId+code+businessId 查询；
     * 返回 flowStatus/statusText/currentNode/currentApprover；无结果返回空列表；
     * 不查流程图、不查历史明细；单批不超过 500。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/batch-status")
    InnerResult<List<ApprovalStatusVO>> batchGetApprovalStatus(
            @Valid @RequestBody ApprovalStatusQueryDTO dto);

    /**
     * 场景：审批人同意任务。
     * 规则：taskId 必须是当前操作人的待办；idempotencyKey 必填；
     * expectedRevision 不为空时按乐观锁校验，冲突返回 999633；
     * 成功后任务完成、实例推进到下一节点，最后节点完成时实例状态变为终态。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/approve")
    InnerResult<TaskHandleResult> approve(@Valid @RequestBody ApprovalActionDTO dto);

    /**
     * 场景：审批人驳回任务。
     * 规则：taskId 必须是当前操作人的待办；idempotencyKey 必填；
     * 驳回后任务完成、实例状态置为 REJECTED(9)；不产生下一节点任务。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/reject")
    InnerResult<TaskHandleResult> reject(@Valid @RequestBody ApprovalActionDTO dto);

    /**
     * 场景：发起人撤回自己提交的审批。
     * 规则：operatorId 必须是实例 starterId；实例状态必须为 RUNNING(1)；
     * 终态实例不可撤回；idempotencyKey 必填；
     * 撤回会完成全部运行中的任务、实例状态置为 WITHDRAWN(6)，并写入一条撤回历史。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/cancel")
    InnerResult<ApprovalStatusVO> cancelApproval(@Valid @RequestBody CancelApprovalDTO dto);

    /**
     * 场景：业务单据详情页展示审批时间线。
     * 规则：按 tenantId+flowCode+businessId 定位实例；实例不存在返回 999610；
     * 返回按 updateTime 倒序的历史记录，包含 node/handler/action/comment/time；
     * limit 为空默认 50，最大 200；无记录返回空列表。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/history")
    InnerResult<List<HistoryVO>> getApprovalHistory(@Valid @RequestBody ApprovalHistoryQueryDTO dto);

    /**
     * 场景：绑定前看定义图；审批中看当前进度图。
     * 规则：businessId 为空时只返回定义图；businessId 不为空时定位实例并返回当前节点、
     * 当前办理人、已走节点和审批历史；实例不存在返回 999610；定义不存在返回 999600。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/approvals/diagram")
    InnerResult<ApprovalDiagramVO> getApprovalDiagram(@Valid @RequestBody ApprovalDiagramQueryDTO dto);

    /**
     * 场景：业务系统渲染当前用户待办列表。
     * 规则：assigneeId 必填；游标分页，cursor 由服务返回；keyword/flowCode 可选；
     * 只返回当前用户可办理的任务；每页不超过 200。
     */
    @PostMapping(WorkflowConstant.BASE_PATH + "/tasks/todo-page")
    InnerResult<CursorPageVO<TaskVO>> pageTodoTasks(@Valid @RequestBody TodoCursorQueryDTO dto);
}
