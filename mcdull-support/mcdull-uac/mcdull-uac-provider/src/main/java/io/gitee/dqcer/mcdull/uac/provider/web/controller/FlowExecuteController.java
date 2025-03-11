package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "流程实例")
@RequestMapping("flow/execute")
public class FlowExecuteController extends BasicController {

//
//    @Resource
//    private TaskService taskService;
//
//    @Operation(summary = "分页待办任务列表")
//    @GetMapping("/toDoPage")
//    public Result<PagedVO<FlowTaskVo>> toDoPage(FlowTask flowTask) {
//        return getDataTable(list);
//    }
//
//    /**
//     * 根据taskId查询代表任务
//     *
//     * @param taskId
//     * @return
//     */
//    @GetMapping("/getTaskById/{taskId}")
//    public Result<Task> getTaskById(@PathVariable("taskId") Long taskId) {
//        return Result.success(taskService.getById(taskId));
//    }
//
//    /**
//     * 查询跳转任意节点列表
//     */
//    @PreAuthorize("@ss.hasPermi('flow:execute:doneList')")
//    @GetMapping("/anyNodeList/{instanceId}")
//    public Result<List<Node>> anyNodeList(@PathVariable("instanceId") Long instanceId) {
//        Instance instance = insService.getById(instanceId);
//        List<Node> nodeList = nodeService.list(FlowEngine.newNode().setDefinitionId(instance.getDefinitionId()));
//        nodeList.removeIf(node -> NodeType.isGateWay(node.getNodeType()));
//        return Result.success(nodeList);
//    }
//
//    /**
//     * 处理非办理的流程交互类型
//     *
//     * @param warmFlowInteractiveTypeVo 要转办用户
//     * @return 是否成功
//     */
//    @PostMapping("/interactiveType")
//    public Result<Boolean> interactiveType(WarmFlowInteractiveTypeVo warmFlowInteractiveTypeVo) {
//        return Result.success(hhDefService.interactiveType(warmFlowInteractiveTypeVo));
//    }
}
