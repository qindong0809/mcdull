package io.gitee.dqcer.mcdull.workflow.provider.application;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.workflow.web.service.IFlowDefinitionService;
import org.dromara.warm.flow.core.FlowEngine;
import org.dromara.warm.flow.core.dto.FlowCombine;
import org.dromara.warm.flow.core.dto.FlowParams;
import org.dromara.warm.flow.core.entity.Definition;
import org.dromara.warm.flow.core.entity.HisTask;
import org.dromara.warm.flow.core.entity.Instance;
import org.dromara.warm.flow.core.entity.Node;
import org.dromara.warm.flow.core.entity.Skip;
import org.dromara.warm.flow.core.entity.Task;
import org.dromara.warm.flow.core.enums.SkipType;
import org.dromara.warm.flow.core.enums.UserType;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.enums.TaskActionEnum;
import io.gitee.dqcer.mcdull.workflow.facade.enums.WorkflowCodeEnum;
import io.gitee.dqcer.mcdull.workflow.facade.enums.WorkflowStatusEnum;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryItem;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ItemResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalDiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalHistoryQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalStatusQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchHandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.CancelApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchStartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionVersionBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HistoryBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TaskBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCountBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCursorQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalDiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVersionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.HistoryVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.InstanceStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.LatestApprovalVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.StartProcessResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskHandleResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TodoCountVO;
import io.gitee.dqcer.mcdull.workflow.web.mapper.WorkflowFacadeMapper;
import jakarta.annotation.Resource;
import org.dromara.warm.flow.core.service.DefService;
import org.dromara.warm.flow.orm.mapper.FlowDefinitionMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * workflow facade 用例实现。
 * <p>
 * 只读写 flow_* 表；用户、组织、业务单据信息全部由调用方以快照形式传入。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Service
public class WorkflowApplicationServiceImpl implements WorkflowApplicationService {

    @Resource
    private WorkflowFacadeMapper workflowFacadeMapper;

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DefService defService;


    @Override
    public List<DefinitionVO> batchGetDefinitions(DefinitionQueryDTO dto) {
        List<Definition> definitions = defService.getByFlowCode(dto.getCode());
        if (definitions == null || definitions.isEmpty()) {
            return Collections.emptyList();
        }
        return definitions.stream()
                .map(definition -> toDefinitionVO(dto.getCode(), definition))
                .collect(Collectors.toList());
    }

    @Override
    public BatchQueryResult<String, DefinitionVersionVO> batchGetDefinitionVersions(DefinitionVersionBatchDTO dto) {
        checkReadSize(dto.getDefinitionKeys().size());
        List<String> codes = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (String key : dto.getDefinitionKeys()) {
            if (StrUtil.startWith(key, "def_")) {
                try {
                    ids.add(Long.valueOf(key.substring(4)));
                    continue;
                } catch (NumberFormatException ignored) {
                    // 非法 def_ 前缀按 flowCode 处理
                }
            }
            codes.add(key);
        }
        Map<String, Definition> byKey = new LinkedHashMap<>();
        if (!codes.isEmpty()) {
            List<Definition> definitions = defService.queryByCodeList(codes);
            if (definitions != null) {
                for (Definition definition : definitions) {
                    byKey.putIfAbsent(definition.getFlowCode(), definition);
                }
            }
        }
        if (!ids.isEmpty()) {
            List<Definition> definitions = defService.getByIds(ids);
            if (definitions != null) {
                for (Definition definition : definitions) {
                    byKey.put("def_" + definition.getId(), definition);
                }
            }
        }
        BatchQueryResult<String, DefinitionVersionVO> result = BatchQueryResult.empty();
        for (String key : dto.getDefinitionKeys()) {
            Definition definition = byKey.get(key);
            result.add(key, definition == null ? null : DefinitionVersionVO.builder()
                    .definitionKey(key)
                    .version(definition.getVersion())
                    .updateTime(definition.getUpdateTime() == null
                            ? null : DateUtil.format(definition.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"))
                    .publishStatus(definition.getIsPublish() == null ? 0 : definition.getIsPublish())
                    .activityStatus(definition.getActivityStatus() == null ? 1 : definition.getActivityStatus())
                    .build());
        }
        return result;
    }

    @Override
    public List<DiagramVO> batchGetDiagrams(DiagramQueryDTO dto) {
        String tenant = tenant(dto.getTenantId());
        List<Definition> definitions = defService.getByFlowCode(dto.getCode());
        if (definitions == null || definitions.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = definitions.stream()
                .map(Definition::getId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toList());
        Map<Long, List<DiagramVO.DiagramNodeVO>> nodes = new LinkedHashMap<>();
        Map<Long, List<DiagramVO.DiagramEdgeVO>> edges = new LinkedHashMap<>();
        if (!ids.isEmpty()) {
            for (Map<String, Object> row : workflowFacadeMapper.selectDefinitionNodes(tenant, ids)) {
                Long definitionId = longOf(row.get("definitionId"));
                nodes.computeIfAbsent(definitionId, k -> new ArrayList<>()).add(DiagramVO.DiagramNodeVO.builder()
                        .nodeCode(str(row.get("nodeCode")))
                        .nodeName(str(row.get("nodeName")))
                        .nodeType(intOf(row.get("nodeType"), 0))
                        .coordinate(str(row.get("coordinate")))
                        .handlerPath(str(row.get("handlerPath")))
                        .permissionFlag(str(row.get("permissionFlag")))
                        .build());
            }
            for (Map<String, Object> row : workflowFacadeMapper.selectDefinitionSkips(tenant, ids)) {
                Long definitionId = longOf(row.get("definitionId"));
                edges.computeIfAbsent(definitionId, k -> new ArrayList<>()).add(DiagramVO.DiagramEdgeVO.builder()
                        .fromNodeCode(str(row.get("fromNodeCode")))
                        .toNodeCode(str(row.get("toNodeCode")))
                        .label(str(row.get("label")))
                        .condition(str(row.get("condition")))
                        .skipType(str(row.get("skipType")))
                        .build());
            }
        }
        return definitions.stream()
                .map(definition -> DiagramVO.builder()
                        .definitionKey(dto.getCode())
                        .nodes(nodes.getOrDefault(definition.getId(), Collections.emptyList()))
                        .edges(edges.getOrDefault(definition.getId(), Collections.emptyList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StartProcessResult startProcess(StartProcessDTO dto) {
        String tenant = tenant(dto.getTenantId());
        return withIdempotency(tenant, dto.getIdempotencyKey(), dto, StartProcessResult.class, () -> doStart(tenant, dto));
    }

    @Override
    public BatchResult<StartProcessResult> batchStartProcesses(BatchStartProcessDTO dto) {
        checkWriteSize(dto.getItems().size());
        List<ItemResult<StartProcessResult>> items = new ArrayList<>();
        for (StartProcessDTO raw : dto.getItems()) {
            StartProcessDTO item = inheritStart(raw, dto);
            String itemKey = StrUtil.isNotBlank(raw.getIdempotencyKey())
                    ? raw.getIdempotencyKey()
                    : raw.getFlowCode() + ":" + raw.getBusinessId();
            try {
                items.add(ItemResult.success(itemKey, this.startProcess(item)));
            } catch (BusinessException e) {
                items.add(ItemResult.fail(itemKey, codeOf(e), e.getMessage()));
            } catch (RuntimeException e) {
                items.add(ItemResult.fail(itemKey, WorkflowCodeEnum.ENGINE_ERROR.getCode(), e.getMessage()));
            }
        }
        return BatchResult.of(items);
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstancesByBusinessKeys(BusinessKeyBatchQueryDTO dto) {
        return queryInstanceStatuses(dto);
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstanceStatuses(BusinessKeyBatchQueryDTO dto) {
        return queryInstanceStatuses(dto);
    }

    @Override
    public List<TodoCountVO> batchCountTodo(TodoCountBatchDTO dto) {
        checkReadSize(dto.getAssigneeIds().size());
        String tenant = tenant(dto.getTenantId());
        List<String> assignees = new ArrayList<>(new LinkedHashSet<>(dto.getAssigneeIds()));
        List<Map<String, Object>> rows = workflowFacadeMapper.selectTodoCounts(
                tenant, assignees, dto.getFlowCodes(), dto.getGroupBy());
        List<TodoCountVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(TodoCountVO.builder()
                    .assigneeId(str(row.get("assigneeId")))
                    .flowCode(str(row.get("flowCode")))
                    .count(intOf(row.get("cnt"), 0))
                    .build());
        }
        return result;
    }

    @Override
    public CursorPageVO<TaskVO> pageTodoTasks(TodoCursorQueryDTO dto) {
        String tenant = tenant(dto.getTenantId());
        int limit = dto.getPageSize() == null ? 50 : Math.min(dto.getPageSize(), WorkflowConstant.MAX_PAGE_SIZE);
        String[] cursor = parseCursor(dto.getCursor());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectTodoPage(
                tenant, dto.getAssigneeId(), dto.getKeyword(), dto.getFlowCode(),
                cursor[0], cursor.length > 1 ? Convert.toLong(cursor[1]) : null, limit);
        List<Long> pageTaskIds = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            pageTaskIds.add(longOf(row.get("taskId")));
        }
        Map<Long, List<String>> assigneeIds = new LinkedHashMap<>();
        Map<Long, List<String>> assigneeNames = new LinkedHashMap<>();
        if (!pageTaskIds.isEmpty()) {
            collectAssignees(workflowFacadeMapper.selectAssigneesByIds(tenant, pageTaskIds),
                    assigneeIds, assigneeNames);
        }
        List<TaskVO> list = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            list.add(toTaskVO(row, assigneeIds, assigneeNames));
        }
        String nextCursor = "";
        if (!rows.isEmpty() && list.size() == limit) {
            Map<String, Object> last = rows.get(rows.size() - 1);
            nextCursor = time(last.get("createTime")) + "|" + str(last.get("taskId"));
        }
        Long total = null;
        if (dto.isNeedTotal()) {
            total = workflowFacadeMapper.countTodo(tenant, dto.getAssigneeId(), dto.getKeyword(), dto.getFlowCode());
        }
        return CursorPageVO.<TaskVO>builder()
                .list(list)
                .nextCursor(nextCursor)
                .hasMore(!rows.isEmpty() && list.size() == limit)
                .total(total)
                .build();
    }

    @Override
    public BatchQueryResult<Long, TaskVO> batchGetTasks(TaskBatchQueryDTO dto) {
        checkReadSize(dto.getTaskIds().size());
        String tenant = tenant(dto.getTenantId());
        List<Long> ids = new ArrayList<>(new LinkedHashSet<>(dto.getTaskIds()));
        List<Map<String, Object>> rows = workflowFacadeMapper.selectTasksByIds(tenant, ids, null);
        Map<Long, List<String>> assigneeIds = new LinkedHashMap<>();
        Map<Long, List<String>> assigneeNames = new LinkedHashMap<>();
        collectAssignees(workflowFacadeMapper.selectAssigneesByIds(tenant, ids), assigneeIds, assigneeNames);
        Map<Long, TaskVO> byId = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            Long taskId = longOf(row.get("taskId"));
            byId.put(taskId, toTaskVO(row, assigneeIds, assigneeNames));
        }
        BatchQueryResult<Long, TaskVO> result = BatchQueryResult.empty();
        for (Long id : dto.getTaskIds()) {
            result.add(id, byId.get(id));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskHandleResult handleTask(HandleTaskDTO dto) {
        String tenant = tenant(dto.getTenantId());
        return withIdempotency(tenant, dto.getIdempotencyKey(), dto, TaskHandleResult.class, () -> doHandle(tenant, dto));
    }

    @Override
    public BatchResult<TaskHandleResult> batchHandleTasks(BatchHandleTaskDTO dto) {
        checkWriteSize(dto.getItems().size());
        List<ItemResult<TaskHandleResult>> items = new ArrayList<>();
        for (HandleTaskDTO raw : dto.getItems()) {
            HandleTaskDTO item = inheritHandle(raw, dto);
            String itemKey = StrUtil.isNotBlank(raw.getIdempotencyKey())
                    ? raw.getIdempotencyKey()
                    : str(raw.getTaskId());
            try {
                items.add(ItemResult.success(itemKey, this.handleTask(item)));
            } catch (BusinessException e) {
                items.add(ItemResult.fail(itemKey, codeOf(e), e.getMessage()));
            } catch (RuntimeException e) {
                items.add(ItemResult.fail(itemKey, WorkflowCodeEnum.ENGINE_ERROR.getCode(), e.getMessage()));
            }
        }
        return BatchResult.of(items);
    }

    @Override
    public List<ApprovalStatusVO> batchGetApprovalStatus(ApprovalStatusQueryDTO dto) {
        checkReadSize(dto.getBusinessIds().size());
        BusinessKeyBatchQueryDTO query = new BusinessKeyBatchQueryDTO();
        query.setTenantId(dto.getTenantId());
        query.setKeys(dto.getBusinessIds().stream()
                .map(businessId -> ProcessBusinessKey.builder()
                        .tenantId(dto.getTenantId())
                        .flowCode(dto.getCode())
                        .businessId(businessId)
                        .build())
                .collect(Collectors.toList()));
        BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> source = batchGetInstanceStatuses(query);
        return source.getItems().stream()
                .map(BatchQueryItem::getValue)
                .filter(value -> value != null)
                .map(value -> toApprovalStatusVO(dto.getTenantId(), value))
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryVO> getApprovalHistory(ApprovalHistoryQueryDTO dto) {
        String tenant = tenant(dto.getTenantId());
        Map<String, Object> instance = workflowFacadeMapper.selectInstanceByBusinessKey(
                tenant, dto.getCode(), dto.getBusinessId());
        Long instanceId = instance == null ? null : longOf(instance.get("id"));
        if (instanceId == null || instanceId <= 0) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        int limit = dto.getLimit() == null || dto.getLimit() <= 0
                ? 50 : Math.min(dto.getLimit(), WorkflowConstant.MAX_PAGE_SIZE);
        List<HisTask> rows = FlowEngine.hisTaskService().getByInsId(instanceId);
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }
        return rows.stream()
                .limit(limit)
                .map(row -> toHistoryVO(instanceId, row))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalStatusVO cancelApproval(CancelApprovalDTO dto) {
        String tenant = tenant(dto.getTenantId());
        return withIdempotency(tenant, dto.getIdempotencyKey(), dto, ApprovalStatusVO.class,
                () -> doCancel(tenant, dto));
    }

    private ApprovalStatusVO doCancel(String tenant, CancelApprovalDTO dto) {
        Map<String, Object> instance = workflowFacadeMapper.selectInstanceByBusinessKey(
                tenant, dto.getCode(), dto.getBusinessId());
        if (instance == null) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        String operator = dto.getOperatorId();
        if (StrUtil.isBlank(operator) || !operator.equals(str(instance.get("starter_id")))) {
            throw new BusinessException(WorkflowCodeEnum.PERMISSION_DENIED);
        }
        if (!WorkflowStatusEnum.RUNNING.getCode().equals(str(instance.get("flow_status")))) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_STATUS_INVALID);
        }
        Long instanceId = longOf(instance.get("id"));
        if (instanceId == null || instanceId <= 0) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }

        String withdrawn = WorkflowStatusEnum.WITHDRAWN.getCode();
        FlowParams params = FlowParams.build()
                .handler(operator)
                .message(dto.getReason())
                .flowStatus(withdrawn)
                .ignore(true);
        FlowEngine.taskService().terminationByInsId(instanceId, params);
        insertEvent(tenant, "PROCESS_WITHDRAWN", str(instanceId), instance);
        return ApprovalStatusVO.builder()
                .tenantId(tenant)
                .instanceId(instanceId)
                .flowCode(dto.getCode())
                .businessId(dto.getBusinessId())
                .businessTitle(str(instance.get("business_title")))
                .flowStatus(withdrawn)
                .statusText(WorkflowStatusEnum.WITHDRAWN.getText())
                .currentNodeCode("")
                .currentNodeName("")
                .currentApproverIds(Collections.emptyList())
                .currentApproverNames(Collections.emptyList())
                .updateTime(DateUtil.now())
                .build();
    }

    @Override
    public ApprovalDiagramVO getApprovalDiagram(ApprovalDiagramQueryDTO dto) {
        String tenant = tenant(dto.getTenantId());
        Map<String, Object> instance = null;
        if (StrUtil.isNotBlank(dto.getBusinessId())) {
            instance = workflowFacadeMapper.selectInstanceByBusinessKey(
                    tenant, dto.getCode(), dto.getBusinessId());
            if (instance == null) {
                throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
            }
        }

        Definition definition = null;
        if (instance != null) {
            Long definitionId = longOf(instance.get("definition_id"));
            if (definitionId != null && definitionId > 0) {
                definition = defService.getById(definitionId);
            }
        } else {
            List<Definition> definitions = defService.getByFlowCode(dto.getCode());
            if (definitions != null && !definitions.isEmpty()) {
                definition = definitions.get(0);
            }
        }
        if (definition == null) {
            throw new BusinessException(WorkflowCodeEnum.DEFINITION_NOT_FOUND);
        }
        DiagramVO diagram = toDiagramVO(dto.getCode(), definition);

        ApprovalDiagramVO.ApprovalDiagramVOBuilder builder = ApprovalDiagramVO.builder()
                .definitionKey(dto.getCode())
                .nodes(diagram.getNodes())
                .edges(diagram.getEdges())
                .businessId(dto.getBusinessId());
        if (instance == null) {
            return builder.build();
        }

        Long instanceId = longOf(instance.get("id"));
        if (instanceId == null || instanceId <= 0) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        String flowStatus = str(instance.get("flow_status"));
        builder.instanceId(instanceId)
                .flowStatus(flowStatus)
                .statusText(WorkflowStatusEnum.textOf(flowStatus))
                .currentNodeCode(str(instance.get("node_code")))
                .currentNodeName(str(instance.get("node_name")));

        List<String> approverIds = new ArrayList<>();
        List<String> approverNames = new ArrayList<>();
        List<Map<String, Object>> approvers = workflowFacadeMapper.selectApproversByInstanceIds(
                tenant, List.of(instanceId));
        if (approvers != null) {
            for (Map<String, Object> row : approvers) {
                approverIds.add(str(row.get("assigneeId")));
                approverNames.add(str(row.get("assigneeName")));
            }
        }
        builder.currentApproverIds(approverIds).currentApproverNames(approverNames);

        LinkedHashSet<String> visited = new LinkedHashSet<>();
        List<HistoryVO> history = new ArrayList<>();
        List<HisTask> historyRows = FlowEngine.hisTaskService().getByInsId(instanceId);
        if (historyRows != null) {
            for (HisTask row : historyRows) {
                HistoryVO item = toHistoryVO(instanceId, row);
                history.add(item);
                if (StrUtil.isNotBlank(item.getNodeCode())) {
                    visited.add(item.getNodeCode());
                }
            }
        }
        return builder.visitedNodeCodes(new ArrayList<>(visited)).history(history).build();
    }

    @Override
    public BatchQueryResult<Long, List<HistoryVO>> batchGetHistoriesByInstanceIds(HistoryBatchQueryDTO dto) {
        checkReadSize(dto.getInstanceIds().size());
        String tenant = tenant(dto.getTenantId());
        int limit = dto.getLimitPerInstance() == null
                ? 50 : Math.min(dto.getLimitPerInstance(), WorkflowConstant.MAX_PAGE_SIZE);
        List<Map<String, Object>> rows = workflowFacadeMapper.selectHistoriesByInstanceIds(
                tenant, dto.getInstanceIds(), limit);
        Map<Long, List<HistoryVO>> byInstance = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            Long instanceId = longOf(row.get("instanceId"));
            byInstance.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(HistoryVO.builder()
                    .historyId(longOf(row.get("historyId")))
                    .instanceId(instanceId)
                    .nodeCode(str(row.get("nodeCode")))
                    .nodeName(str(row.get("nodeName")))
                    .handlerId(str(row.get("handlerId")))
                    .handlerName(str(row.get("handlerName")))
                    .action(actionOf(row))
                    .skipType(str(row.get("skipType")))
                    .comment(str(row.get("comment")))
                    .createTime(time(row.get("createTime")))
                    .updateTime(time(row.get("updateTime")))
                    .build());
        }
        BatchQueryResult<Long, List<HistoryVO>> result = BatchQueryResult.empty();
        for (Long id : dto.getInstanceIds()) {
            List<HistoryVO> values = byInstance.getOrDefault(id, Collections.emptyList());
            result.add(id, values.size() > limit ? values.subList(0, limit) : values);
        }
        return result;
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> batchGetLatestApprovals(BusinessKeyBatchQueryDTO dto) {
        checkReadSize(dto.getKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectLatestApprovals(tenant, dto.getKeys());
        Map<String, LatestApprovalVO> byKey = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String flowCode = str(row.get("flowCode"));
            String businessId = str(row.get("businessId"));
            String flowStatus = str(row.get("flowStatus"));
            byKey.put(flowCode + "|" + businessId, LatestApprovalVO.builder()
                    .instanceId(longOf(row.get("instanceId")))
                    .flowCode(flowCode)
                    .businessId(businessId)
                    .flowStatus(flowStatus)
                    .statusText(WorkflowStatusEnum.textOf(flowStatus))
                    .handlerId(str(row.get("handlerId")))
                    .handlerName(str(row.get("handlerName")))
                    .action(actionOf(row))
                    .comment(str(row.get("comment")))
                    .updateTime(time(row.get("updateTime")))
                    .build());
        }
        BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> result = BatchQueryResult.empty();
        for (ProcessBusinessKey key : dto.getKeys()) {
            result.add(key, byKey.get(key.getFlowCode() + "|" + key.getBusinessId()));
        }
        return result;
    }

    // ==================== 私有方法 ====================

    private BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> queryInstanceStatuses(BusinessKeyBatchQueryDTO dto) {
        checkReadSize(dto.getKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectInstancesByBusinessKeys(tenant, dto.getKeys());
        Map<String, Map<String, Object>> byKey = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            byKey.put(str(row.get("flowCode")) + "|" + str(row.get("businessId")), row);
        }
        List<Long> instanceIds = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            instanceIds.add(longOf(row.get("id")));
        }
        Map<Long, List<String>> approverIds = new LinkedHashMap<>();
        Map<Long, List<String>> approverNames = new LinkedHashMap<>();
        if (!instanceIds.isEmpty()) {
            List<Map<String, Object>> approvers = workflowFacadeMapper.selectApproversByInstanceIds(tenant, instanceIds);
            for (Map<String, Object> row : approvers) {
                Long instanceId = longOf(row.get("instanceId"));
                approverIds.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(str(row.get("assigneeId")));
                approverNames.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(str(row.get("assigneeName")));
            }
        }
        BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> result = BatchQueryResult.empty();
        for (ProcessBusinessKey key : dto.getKeys()) {
            Map<String, Object> row = byKey.get(key.getFlowCode() + "|" + key.getBusinessId());
            if (row == null) {
                result.add(key, null);
                continue;
            }
            Long instanceId = longOf(row.get("id"));
            String flowStatus = str(row.get("flowStatus"));
            result.add(key, InstanceStatusVO.builder()
                    .instanceId(instanceId)
                    .flowCode(key.getFlowCode())
                    .businessId(key.getBusinessId())
                    .businessType(str(row.get("business_type")))
                    .businessTitle(str(row.get("business_title")))
                    .flowStatus(flowStatus)
                    .statusText(WorkflowStatusEnum.textOf(flowStatus))
                    .currentNodeCode(str(row.get("node_code")))
                    .currentNodeName(str(row.get("node_name")))
                    .currentApproverIds(approverIds.getOrDefault(instanceId, Collections.emptyList()))
                    .currentApproverNames(approverNames.getOrDefault(instanceId, Collections.emptyList()))
                    .starterId(str(row.get("starter_id")))
                    .starterName(str(row.get("starter_name")))
                    .updateTime(time(row.get("update_time")))
                    .build());
        }
        return result;
    }

    private StartProcessResult doStart(String tenant, StartProcessDTO dto) {
        Map<String, Object> exists = workflowFacadeMapper.selectInstanceByBusinessKey(
                tenant, dto.getFlowCode(), dto.getBusinessId());
        if (exists != null && !WorkflowStatusEnum.isFinal(str(exists.get("flow_status")))) {
            throw new BusinessException(WorkflowCodeEnum.BUSINESS_KEY_CONFLICT);
        }

        String operatorId = StrUtil.isBlank(dto.getOperatorId()) ? "0" : dto.getOperatorId();
        String starterName = StrUtil.isBlank(dto.getOperatorName()) ? operatorId : dto.getOperatorName();
        Map<String, Object> ext = new HashMap<>();
        ext.put("starterName", starterName);
        ext.put("businessTitle", dto.getBusinessTitle());
        ext.put("businessType", dto.getBusinessType());

        FlowParams params = FlowParams.build()
                .flowCode(dto.getFlowCode())
                .handler(operatorId)
                .variable(dto.getVariables())
                .flowStatus(WorkflowStatusEnum.RUNNING.getCode())
                .ext(json(ext))
                .ignore(true);
        if (dto.getAssigneeIds() != null && !dto.getAssigneeIds().isEmpty()) {
            params.nextHandler(dto.getAssigneeIds().toArray(String[]::new));
        }
        Instance instance = FlowEngine.insService().start(dto.getBusinessId(), params);

        workflowFacadeMapper.updateInstanceSnapshot(tenant, instance.getId(), dto.getFlowCode(),
                operatorId, starterName, dto.getBusinessTitle(), dto.getBusinessType(), 0L);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", instance.getId());
        payload.put("definitionId", instance.getDefinitionId());
        payload.put("flowCode", dto.getFlowCode());
        payload.put("businessId", instance.getBusinessId());
        payload.put("businessTitle", dto.getBusinessTitle());
        payload.put("businessType", dto.getBusinessType());
        payload.put("nodeType", instance.getNodeType());
        payload.put("nodeCode", instance.getNodeCode());
        payload.put("nodeName", instance.getNodeName());
        payload.put("variable", instance.getVariable());
        payload.put("flowStatus", instance.getFlowStatus());
        payload.put("createBy", instance.getCreateBy());
        payload.put("starterId", operatorId);
        payload.put("starterName", starterName);
        payload.put("tenantId", tenant);
        insertEvent(tenant, "PROCESS_STARTED", str(instance.getId()), payload);

        List<Task> tasks = FlowEngine.taskService().getByInsId(instance.getId());
        if (tasks != null) {
            for (Task task : tasks) {
                Map<String, Object> taskPayload = new HashMap<>();
                taskPayload.put("id", task.getId());
                taskPayload.put("definitionId", task.getDefinitionId());
                taskPayload.put("instanceId", task.getInstanceId());
                taskPayload.put("nodeCode", task.getNodeCode());
                taskPayload.put("nodeName", task.getNodeName());
                taskPayload.put("nodeType", task.getNodeType());
                taskPayload.put("flowStatus", task.getFlowStatus());
                taskPayload.put("tenantId", tenant);
                insertEvent(tenant, "TASK_CREATED", str(task.getId()), taskPayload);
            }
        }
        return StartProcessResult.builder()
                .instanceId(instance.getId())
                .flowCode(dto.getFlowCode())
                .businessId(instance.getBusinessId())
                .flowStatus(instance.getFlowStatus())
                .currentNodeCode(instance.getNodeCode())
                .currentNodeName(instance.getNodeName())
                .createTime(DateUtil.format(instance.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    private TaskHandleResult doHandle(String tenant, HandleTaskDTO dto) {
        TaskActionEnum action = TaskActionEnum.of(dto.getAction());
        if (action == null) {
            throw new BusinessException(WorkflowCodeEnum.TASK_ACTION_INVALID);
        }
        Map<String, Object> task = workflowFacadeMapper.selectTaskForHandle(tenant, dto.getTaskId());
        if (task == null) {
            throw new BusinessException(WorkflowCodeEnum.TASK_NOT_FOUND);
        }
        String instanceStatus = str(task.get("instanceStatus"));
        if (!WorkflowStatusEnum.RUNNING.getCode().equals(instanceStatus)) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_STATUS_INVALID);
        }
        String operator = StrUtil.isBlank(dto.getOperatorId()) ? "0" : dto.getOperatorId();
        List<String> approvers = FlowEngine.userService()
                .getPermission(dto.getTaskId(), UserType.APPROVAL.getKey());
        if (approvers == null || !approvers.contains(operator)) {
            throw new BusinessException(WorkflowCodeEnum.TASK_NOT_ASSIGNED);
        }
        Long expectedRevision = dto.getExpectedRevision() == null
                ? longOf(task.get("taskRevision")) : dto.getExpectedRevision();
        Long expectedInstanceRevision = dto.getExpectedRevision() == null
                ? longOf(task.get("instanceRevision")) : dto.getExpectedRevision();
        Long taskId = longOf(task.get("taskId"));
        Long instanceId = longOf(task.get("instanceId"));
        Long definitionId = longOf(task.get("definitionId"));
        String operatorName = StrUtil.isBlank(dto.getOperatorName()) ? operator : dto.getOperatorName();

        if (action == TaskActionEnum.APPROVE || action == TaskActionEnum.REJECT) {
            return doHandleByWarmFlow(tenant, dto, action, task, instanceId, operator,
                    expectedRevision, expectedInstanceRevision);
        }

        HandlePlan plan = buildPlan(tenant, action, task, definitionId);
        if (action == TaskActionEnum.URGE) {
            insertEvent(tenant, "TASK_URGED", str(taskId), task);
            return TaskHandleResult.builder()
                    .taskId(taskId)
                    .instanceId(instanceId)
                    .action(action.getCode())
                    .flowStatus(instanceStatus)
                    .finished(false)
                    .build();
        }

        Long historyId = IdUtil.getSnowflakeNextId();
        Map<String, Object> history = new HashMap<>();
        history.put("id", historyId);
        history.put("definitionId", definitionId);
        history.put("instanceId", instanceId);
        history.put("taskId", taskId);
        history.put("nodeCode", str(task.get("nodeCode")));
        history.put("nodeName", str(task.get("nodeName")));
        history.put("targetNodeCode", plan.nextCode);
        history.put("approver", operator);
        history.put("approverId", operator);
        history.put("approverName", operatorName);
        history.put("cooperateType", plan.cooperateType);
        history.put("skipType", plan.skipType);
        history.put("flowStatus", plan.flowStatus);
        history.put("message", dto.getComment());
        history.put("variable", json(dto.getVariables()));
        history.put("tenantId", tenant);
        workflowFacadeMapper.insertHistory(history);

        if (plan.insertUsers) {
            List<String> targets = dto.getTargetAssigneeIds() == null || dto.getTargetAssigneeIds().isEmpty()
                    ? Collections.singletonList(operator) : dto.getTargetAssigneeIds();
            List<Map<String, Object>> userRows = new ArrayList<>();
            for (String target : targets) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", IdUtil.getSnowflakeNextId());
                row.put("type", str(plan.cooperateType));
                row.put("processedBy", target);
                row.put("processedName", StrUtil.isBlank(dto.getTargetAssigneeNames().get(target))
                        ? target : dto.getTargetAssigneeNames().get(target));
                row.put("associated", taskId);
                row.put("createBy", operator);
                row.put("tenantId", tenant);
                userRows.add(row);
            }
            workflowFacadeMapper.insertHandleUsers(userRows);
        }

        if (plan.completeTask) {
            int updated = workflowFacadeMapper.completeTask(
                    tenant, taskId, plan.flowStatus, expectedRevision);
            if (updated == 0) {
                throw new BusinessException(WorkflowCodeEnum.CONCURRENT_CONFLICT);
            }
        }
        if (plan.updateInstance) {
            int updated = workflowFacadeMapper.updateInstanceForHandle(
                    tenant, instanceId, plan.flowStatus, plan.nextCode, plan.nextName, expectedInstanceRevision);
            if (updated == 0) {
                throw new BusinessException(WorkflowCodeEnum.CONCURRENT_CONFLICT);
            }
        }
        insertEvent(tenant, "TASK_COMPLETED", str(historyId), history);
        if (WorkflowStatusEnum.isFinal(plan.flowStatus)) {
            insertEvent(tenant, "PROCESS_COMPLETED", str(instanceId), history);
        }
        return TaskHandleResult.builder()
                .taskId(taskId)
                .instanceId(instanceId)
                .historyId(historyId)
                .action(action.getCode())
                .flowStatus(plan.flowStatus)
                .nextNodeCode(plan.nextCode)
                .nextNodeName(plan.nextName)
                .finished(WorkflowStatusEnum.isFinal(plan.flowStatus))
                .build();
    }

    private TaskHandleResult doHandleByWarmFlow(String tenant, HandleTaskDTO dto, TaskActionEnum action,
                                                Map<String, Object> task, Long instanceId, String operator,
                                                Long expectedRevision, Long expectedInstanceRevision) {
        Long currentTaskRevision = longOf(task.get("taskRevision"));
        Long currentInstanceRevision = longOf(task.get("instanceRevision"));
        if (expectedRevision != null && currentTaskRevision != null && !expectedRevision.equals(currentTaskRevision)) {
            throw new BusinessException(WorkflowCodeEnum.CONCURRENT_CONFLICT);
        }
        if (expectedInstanceRevision != null && currentInstanceRevision != null
                && !expectedInstanceRevision.equals(currentInstanceRevision)) {
            throw new BusinessException(WorkflowCodeEnum.CONCURRENT_CONFLICT);
        }

        Long taskId = longOf(task.get("taskId"));
        FlowParams params = FlowParams.build()
                .handler(operator)
                .message(dto.getComment())
                .variable(dto.getVariables())
                .ignore(true);
        if (action == TaskActionEnum.APPROVE) {
            params.skipType(SkipType.PASS.getKey());
        } else {
            params.skipType(SkipType.REJECT.getKey())
                    .flowStatus(WorkflowStatusEnum.REJECTED.getCode())
                    .hisStatus(WorkflowStatusEnum.REJECTED.getCode());
        }
        Instance instance = FlowEngine.taskService().skip(taskId, params);

        List<HisTask> hisTasks = FlowEngine.hisTaskService().listByTaskId(taskId);
        Long historyId = hisTasks == null || hisTasks.isEmpty() ? null : hisTasks.get(hisTasks.size() - 1).getId();
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", historyId);
        payload.put("instanceId", instanceId);
        payload.put("taskId", taskId);
        payload.put("action", action.getCode());
        payload.put("comment", dto.getComment());
        payload.put("variables", dto.getVariables());
        payload.put("flowStatus", instance.getFlowStatus());
        payload.put("tenantId", tenant);
        insertEvent(tenant, "TASK_COMPLETED", str(historyId), payload);
        if (WorkflowStatusEnum.isFinal(instance.getFlowStatus())) {
            insertEvent(tenant, "PROCESS_COMPLETED", str(instanceId), payload);
        }
        return TaskHandleResult.builder()
                .taskId(taskId)
                .instanceId(instanceId)
                .historyId(historyId)
                .action(action.getCode())
                .flowStatus(instance.getFlowStatus())
                .nextNodeCode(instance.getNodeCode())
                .nextNodeName(instance.getNodeName())
                .finished(WorkflowStatusEnum.isFinal(instance.getFlowStatus()))
                .build();
    }

    private HandlePlan buildPlan(String tenant, TaskActionEnum action,
                                 Map<String, Object> task, Long definitionId) {
        HandlePlan plan = new HandlePlan();
        plan.skipType = "NONE";
        plan.cooperateType = 1;
        plan.nextCode = "";
        plan.nextName = "";
        switch (action) {
            case APPROVE -> {
                List<Map<String, Object>> nextNodes = workflowFacadeMapper.selectNextNode(
                        tenant, definitionId, str(task.get("nodeCode")));
                Map<String, Object> next = nextNodes == null || nextNodes.isEmpty() ? null : nextNodes.get(0);
                plan.skipType = "PASS";
                if (next != null && StrUtil.isNotBlank(str(next.get("nodeCode")))) {
                    plan.flowStatus = WorkflowStatusEnum.RUNNING.getCode();
                    plan.nextCode = str(next.get("nodeCode"));
                    plan.nextName = str(next.get("nodeName"));
                } else {
                    plan.flowStatus = WorkflowStatusEnum.COMPLETED.getCode();
                }
                plan.completeTask = true;
                plan.updateInstance = true;
            }
            case REJECT -> {
                plan.flowStatus = WorkflowStatusEnum.REJECTED.getCode();
                plan.skipType = "REJECT";
                plan.completeTask = true;
                plan.updateInstance = true;
            }
            case TRANSFER, DELEGATE, COUNTERSIGN -> {
                plan.flowStatus = WorkflowStatusEnum.RUNNING.getCode();
                plan.cooperateType = action == TaskActionEnum.TRANSFER ? 2
                        : action == TaskActionEnum.DELEGATE ? 3 : 6;
                plan.completeTask = action != TaskActionEnum.COUNTERSIGN;
                plan.updateInstance = false;
                plan.insertUsers = true;
            }
            case WITHDRAW, TAKE_BACK -> {
                plan.flowStatus = action == TaskActionEnum.WITHDRAW
                        ? WorkflowStatusEnum.WITHDRAWN.getCode()
                        : WorkflowStatusEnum.TAKEN_BACK.getCode();
                plan.completeTask = true;
                plan.updateInstance = true;
            }
            case URGE -> plan.flowStatus = WorkflowStatusEnum.RUNNING.getCode();
            default -> throw new BusinessException(WorkflowCodeEnum.TASK_ACTION_INVALID);
        }
        return plan;
    }

    private <T> T withIdempotency(String tenant, String key, Object request, Class<T> type, Supplier<T> action) {
        if (StrUtil.isBlank(key)) {
            return action.get();
        }
        String hash = md5(json(request));
        Map<String, Object> existing = workflowFacadeMapper.selectIdempotency(tenant, key);
        if (existing == null) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", IdUtil.getSnowflakeNextId());
            row.put("tenantId", tenant);
            row.put("idempotencyKey", key);
            row.put("requestHash", hash);
            row.put("tenantId", tenant);
            try {
                workflowFacadeMapper.insertIdempotency(row);
            } catch (DuplicateKeyException e) {
                existing = workflowFacadeMapper.selectIdempotency(tenant, key);
            }
        }
        if (existing != null) {
            if (!hash.equals(str(existing.get("request_hash")))) {
                throw new BusinessException(WorkflowCodeEnum.IDEMPOTENCY_CONFLICT);
            }
            String status = str(existing.get("status"));
            if ("SUCCESS".equals(status)) {
                return fromJson(str(existing.get("response_json")), type);
            }
            if ("PROCESSING".equals(status)) {
                throw new BusinessException(WorkflowCodeEnum.DUPLICATE_REQUEST);
            }
        }
        try {
            T result = action.get();
            workflowFacadeMapper.updateIdempotencySuccess(tenant, key, json(result));
            return result;
        } catch (BusinessException e) {
            workflowFacadeMapper.updateIdempotencyFailed(
                    tenant, key, codeOf(e), e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            workflowFacadeMapper.updateIdempotencyFailed(
                    tenant, key, WorkflowCodeEnum.ENGINE_ERROR.getCode(), e.getMessage());
            throw e;
        }
    }

    private void insertEvent(String tenant, String eventType, String aggregateId, Object payload) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", IdUtil.getSnowflakeNextId());
        row.put("tenantId", tenant);
        row.put("eventType", eventType);
        row.put("aggregateId", aggregateId);
        row.put("payloadJson", json(payload));
        workflowFacadeMapper.insertEvent(row);
    }

    private void insertFlowUser(String tenant, Long taskId, String type,
                                String processedBy, String processedName, String createBy) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", IdUtil.getSnowflakeNextId());
        row.put("type", type);
        row.put("processedBy", processedBy);
        row.put("processedName", processedName);
        row.put("associated", taskId);
        row.put("createBy", createBy);
        row.put("tenantId", tenant);
        workflowFacadeMapper.insertFlowUser(row);
    }

    private List<String> resolveAssignees(StartProcessDTO dto, String handlerPath, String starterId) {
        List<String> result = new ArrayList<>();
        if (dto.getAssigneeIds() != null) {
            for (String id : dto.getAssigneeIds()) {
                if (StrUtil.isNotBlank(id)) {
                    result.add(id.trim());
                }
            }
        }
        if (result.isEmpty() && StrUtil.isNotBlank(handlerPath)) {
            for (String part : handlerPath.split("[,;@]+")) {
                String id = part.trim();
                if (id.startsWith("user:")) {
                    id = id.substring(5);
                }
                if (StrUtil.isNumeric(id)) {
                    result.add(id);
                }
            }
        }
        if (result.isEmpty()) {
            result.add(starterId);
        }
        return new ArrayList<>(new LinkedHashSet<>(result));
    }

    private StartProcessDTO inheritStart(StartProcessDTO item, BaseWorkflowRequest base) {
        if (StrUtil.isBlank(item.getTenantId())) {
            item.setTenantId(base.getTenantId());
        }
        if (StrUtil.isBlank(item.getOperatorId())) {
            item.setOperatorId(base.getOperatorId());
        }
        if (StrUtil.isBlank(item.getOperatorName())) {
            item.setOperatorName(base.getOperatorName());
        }
        if (StrUtil.isBlank(item.getSourceSystem())) {
            item.setSourceSystem(base.getSourceSystem());
        }
        if (StrUtil.isBlank(item.getTraceId())) {
            item.setTraceId(base.getTraceId());
        }
        return item;
    }

    private HandleTaskDTO inheritHandle(HandleTaskDTO item, BaseWorkflowRequest base) {
        if (StrUtil.isBlank(item.getTenantId())) {
            item.setTenantId(base.getTenantId());
        }
        if (StrUtil.isBlank(item.getOperatorId())) {
            item.setOperatorId(base.getOperatorId());
        }
        if (StrUtil.isBlank(item.getOperatorName())) {
            item.setOperatorName(base.getOperatorName());
        }
        if (StrUtil.isBlank(item.getSourceSystem())) {
            item.setSourceSystem(base.getSourceSystem());
        }
        if (StrUtil.isBlank(item.getTraceId())) {
            item.setTraceId(base.getTraceId());
        }
        return item;
    }

    private DefinitionVO toDefinitionVO(String key, Definition definition) {
        return DefinitionVO.builder()
                .definitionKey(key)
                .definitionId(definition.getId())
                .flowCode(definition.getFlowCode())
                .flowName(definition.getFlowName())
                .category(definition.getCategory())
                .version(definition.getVersion())
                .publishStatus(definition.getIsPublish() == null ? 0 : definition.getIsPublish())
                .activityStatus(definition.getActivityStatus() == null ? 1 : definition.getActivityStatus())
                .formCustom(definition.getFormCustom())
                .formPath(definition.getFormPath())
                .ext(definition.getExt())
                .updateTime(definition.getUpdateTime() == null
                        ? null : DateUtil.format(definition.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    private ApprovalStatusVO toApprovalStatusVO(String tenant, InstanceStatusVO value) {
        return ApprovalStatusVO.builder()
                .tenantId(tenant)
                .instanceId(value.getInstanceId())
                .flowCode(value.getFlowCode())
                .businessId(value.getBusinessId())
                .businessTitle(value.getBusinessTitle())
                .flowStatus(value.getFlowStatus())
                .statusText(value.getStatusText())
                .currentNodeCode(value.getCurrentNodeCode())
                .currentNodeName(value.getCurrentNodeName())
                .currentApproverIds(value.getCurrentApproverIds())
                .currentApproverNames(value.getCurrentApproverNames())
                .updateTime(value.getUpdateTime())
                .build();
    }

    private DiagramVO toDiagramVO(String code, Definition definition) {
        FlowCombine combine = defService.getFlowCombineNoDef(definition.getId());
        List<DiagramVO.DiagramNodeVO> nodes = new ArrayList<>();
        List<DiagramVO.DiagramEdgeVO> edges = new ArrayList<>();
        if (combine != null) {
            for (Node node : combine.getAllNodes()) {
                nodes.add(DiagramVO.DiagramNodeVO.builder()
                        .nodeCode(node.getNodeCode())
                        .nodeName(node.getNodeName())
                        .nodeType(node.getNodeType())
                        .coordinate(node.getCoordinate())
                        .handlerPath(node.getListenerPath())
                        .permissionFlag(node.getPermissionFlag())
                        .build());
            }
            for (Skip skip : combine.getAllSkips()) {
                edges.add(DiagramVO.DiagramEdgeVO.builder()
                        .fromNodeCode(skip.getNowNodeCode())
                        .toNodeCode(skip.getNextNodeCode())
                        .label(skip.getSkipName())
                        .condition(skip.getSkipCondition())
                        .skipType(skip.getSkipType())
                        .build());
            }
        }
        return DiagramVO.builder()
                .definitionKey(code)
                .nodes(nodes)
                .edges(edges)
                .build();
    }

    private TaskVO toTaskVO(Map<String, Object> row,
                            Map<Long, List<String>> assigneeIds,
                            Map<Long, List<String>> assigneeNames) {
        Long taskId = longOf(row.get("taskId"));
        return TaskVO.builder()
                .taskId(taskId)
                .instanceId(longOf(row.get("instanceId")))
                .definitionId(longOf(row.get("definitionId")))
                .flowCode(str(row.get("flowCode")))
                .flowStatus(str(row.get("flowStatus")))
                .nodeCode(str(row.get("nodeCode")))
                .nodeName(str(row.get("nodeName")))
                .businessId(str(row.get("businessId")))
                .businessTitle(str(row.get("businessTitle")))
                .starterId(str(row.get("starterId")))
                .starterName(str(row.get("starterName")))
                .assigneeIds(assigneeIds.getOrDefault(taskId, Collections.emptyList()))
                .assigneeNames(assigneeNames.getOrDefault(taskId, Collections.emptyList()))
                .createTime(time(row.get("createTime")))
                .updateTime(time(row.get("updateTime")))
                .revision(longOf(row.get("revision")))
                .build();
    }

    private void collectAssignees(List<Map<String, Object>> rows,
                                  Map<Long, List<String>> ids,
                                  Map<Long, List<String>> names) {
        for (Map<String, Object> row : rows) {
            Long taskId = longOf(row.get("taskId"));
            ids.computeIfAbsent(taskId, k -> new ArrayList<>()).add(str(row.get("assigneeId")));
            names.computeIfAbsent(taskId, k -> new ArrayList<>()).add(str(row.get("assigneeName")));
        }
    }

    private HistoryVO toHistoryVO(Long instanceId, HisTask row) {
        Map<String, Object> value = new HashMap<>();
        value.put("flowStatus", row.getFlowStatus());
        value.put("cooperateType", row.getCooperateType());
        value.put("skipType", row.getSkipType());
        return HistoryVO.builder()
                .historyId(row.getId())
                .instanceId(instanceId)
                .nodeCode(row.getNodeCode())
                .nodeName(row.getNodeName())
                .handlerId(row.getApprover())
                .handlerName(row.getApprover())
                .action(actionOf(value))
                .skipType(row.getSkipType())
                .comment(row.getMessage())
                .createTime(row.getCreateTime() == null
                        ? null : DateUtil.format(row.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .updateTime(row.getUpdateTime() == null
                        ? null : DateUtil.format(row.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    private String actionOf(Map<String, Object> row) {
        int cooperateType = intOf(row.get("cooperateType"), 1);
        String skipType = str(row.get("skipType"));
        if (WorkflowStatusEnum.WITHDRAWN.getCode().equals(str(row.get("flowStatus")))) {
            return TaskActionEnum.WITHDRAW.getCode();
        }
        if (cooperateType == 2) {
            return TaskActionEnum.TRANSFER.getCode();
        }
        if (cooperateType == 3) {
            return TaskActionEnum.DELEGATE.getCode();
        }
        if (cooperateType == 6) {
            return TaskActionEnum.COUNTERSIGN.getCode();
        }
        if ("REJECT".equals(skipType)) {
            return TaskActionEnum.REJECT.getCode();
        }
        if ("WITHDRAW".equals(skipType)) {
            return TaskActionEnum.WITHDRAW.getCode();
        }
        if ("PASS".equals(skipType)) {
            return TaskActionEnum.APPROVE.getCode();
        }
        return TaskActionEnum.APPROVE.getCode();
    }

    private String[] parseCursor(String cursor) {
        if (StrUtil.isBlank(cursor)) {
            return new String[]{"", "0"};
        }
        String[] parts = cursor.split("\\|", 2);
        return parts.length == 2 ? parts : new String[]{parts[0], "0"};
    }

    private void checkReadSize(int size) {
        if (size > WorkflowConstant.MAX_BATCH_READ_SIZE) {
            throw new BusinessException(WorkflowCodeEnum.BATCH_SIZE_EXCEEDED);
        }
    }

    private void checkWriteSize(int size) {
        if (size > WorkflowConstant.MAX_BATCH_WRITE_SIZE) {
            throw new BusinessException(WorkflowCodeEnum.BATCH_SIZE_EXCEEDED);
        }
    }

    private int codeOf(BusinessException e) {
        return e.getCode() == null ? WorkflowCodeEnum.ENGINE_ERROR.getCode() : e.getCode().getCode();
    }

    private String tenant(String tenant) {
        return StrUtil.isBlank(tenant) ? "0" : tenant;
    }

    private String str(Object value) {
        return Convert.toStr(value, "");
    }

    private long longOf(Object value) {
        return Convert.toLong(value, 0L);
    }

    private int intOf(Object value, int defaultValue) {
        return Convert.toInt(value, defaultValue);
    }

    private String time(Object value) {
        return value == null ? "" : Convert.toStr(value);
    }

    private String json(Object value) {
        try {
            return objectMapper.writeValueAsString(value == null ? Collections.emptyMap() : value);
        } catch (Exception e) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
    }

    private <T> T fromJson(String value, Class<T> type) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return objectMapper.readValue(value, type);
        } catch (Exception e) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
    }

    private String md5(String value) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                builder.append(String.format("%02x", b & 0xff));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
    }

    private static class HandlePlan {
        private String flowStatus;
        private String skipType;
        private int cooperateType;
        private String nextCode;
        private String nextName;
        private boolean completeTask;
        private boolean updateInstance;
        private boolean insertUsers;
    }
}
