package io.gitee.dqcer.mcdull.workflow.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.workflow.web.service.IWorkflowCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工作流中心：概览、定义、设计器、实例、任务、历史、流程图
 *
 * @author dqcer
 */
@Tag(name = "Workflow center")
@RestController
@RequestMapping("/workflow/center")
public class WorkflowCenterController extends BasicController {

    @Resource
    private IWorkflowCenterService workflowCenterService;

    @Operation(summary = "工作流概览")
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        return Result.success(workflowCenterService.overview());
    }

    @Operation(summary = "流程定义列表")
    @PostMapping("/definition/list")
    public Result<PagedVO<Map<String, Object>>> definitionList(@RequestBody Map<String, Object> param) {
        return Result.success(workflowCenterService.definitionList(param));
    }

    @Operation(summary = "流程定义详情")
    @GetMapping("/definition/{id}")
    public Result<Map<String, Object>> definitionDetail(@PathVariable("id") String id) {
        return Result.success(workflowCenterService.definitionDetail(id));
    }

    @Operation(summary = "保存设计器画布（节点与跳线）")
    @PostMapping("/definition/graph")
    public Result<Boolean> saveGraph(@RequestBody Map<String, Object> param) {
        return Result.success(workflowCenterService.saveGraph(param));
    }

    @Operation(summary = "流程实例列表")
    @PostMapping("/instance/list")
    public Result<PagedVO<Map<String, Object>>> instanceList(@RequestBody Map<String, Object> param) {
        return Result.success(workflowCenterService.instanceList(param));
    }

    @Operation(summary = "流程实例详情")
    @GetMapping("/instance/{id}")
    public Result<Map<String, Object>> instanceDetail(@PathVariable("id") String id) {
        return Result.success(workflowCenterService.instanceDetail(id));
    }

    @Operation(summary = "终止流程实例")
    @PostMapping("/instance/terminate/{id}")
    public Result<Boolean> terminate(@PathVariable("id") String id) {
        return Result.success(workflowCenterService.terminate(id));
    }

    @Operation(summary = "催办")
    @PostMapping("/instance/urge/{id}")
    public Result<Boolean> urge(@PathVariable("id") String id) {
        return Result.success(workflowCenterService.urge(id));
    }

    @Operation(summary = "我的待办列表")
    @PostMapping("/task/list")
    public Result<PagedVO<Map<String, Object>>> taskList(@RequestBody Map<String, Object> param) {
        return Result.success(workflowCenterService.taskList(param));
    }

    @Operation(summary = "抄送我的")
    @GetMapping("/task/copied")
    public Result<List<Map<String, Object>>> copied() {
        return Result.success(workflowCenterService.copied());
    }

    @Operation(summary = "我发起的")
    @GetMapping("/task/started")
    public Result<List<Map<String, Object>>> started() {
        return Result.success(workflowCenterService.started());
    }

    @Operation(summary = "办理任务")
    @PostMapping("/task/handle")
    public Result<Boolean> handleTask(@RequestBody Map<String, Object> param) {
        return Result.success(workflowCenterService.handleTask(param));
    }

    @Operation(summary = "历史任务列表")
    @PostMapping("/history/list")
    public Result<PagedVO<Map<String, Object>>> historyList(@RequestBody Map<String, Object> param) {
        return Result.success(workflowCenterService.historyList(param));
    }

    @Operation(summary = "流程图数据")
    @GetMapping("/diagram/{definitionId}")
    public Result<Map<String, Object>> diagram(@PathVariable("definitionId") String definitionId) {
        return Result.success(workflowCenterService.diagram(definitionId));
    }
}
