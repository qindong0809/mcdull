//package io.gitee.dqcer.mcdull.system.provider.web.controller.workflow;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.stream.StreamUtil;
//import cn.hutool.core.util.ObjUtil;
//import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
//import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
//import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
//import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
//import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
//import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
//import io.gitee.dqcer.mcdull.system.provider.model.vo.workflow.FlowTaskVO;
//import io.gitee.dqcer.mcdull.system.provider.web.service.IUserRoleService;
//import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.annotation.Resource;
//import org.dromara.warm.flow.core.service.TaskService;
//import org.dromara.warm.flow.core.utils.StreamUtils;
//import org.dromara.warm.flow.orm.entity.FlowTask;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@Tag(name = "流程实例")
//@RequestMapping("flow/execute")
//public class FlowExecuteController extends BasicController {
//
//
//    @Resource
//    private TaskService taskService;
//    @Resource
//    private IUserRoleService userRoleService;
//    @Resource
//    private IUserService userService;
//
//
//    private List<String> permissionList(Integer userId) {
//        List<String> permissionList = new ArrayList<>();
//        Map<Integer, List<Integer>> roleIdListMap = userRoleService.getRoleIdListMap(ListUtil.of(userId));
//        List<Integer> roleIdList = roleIdListMap.get(userId);
//        if (CollUtil.isNotEmpty(roleIdList)) {
//            permissionList = StreamUtils.toList(roleIdList, roleId -> "role:" + roleId);
//        }
//        permissionList.add(userId.toString());
//        UserEntity user = userService.get(userId);
//        if (ObjUtil.isNotNull(user)) {
//            Integer departmentId = user.getDepartmentId();
//            if (ObjUtil.isNotNull(departmentId)) {
//                permissionList.add("dept:" + departmentId);
//            }
//        }
//        LogHelp.info(log, "当前用户所有权限[{}]", permissionList);
//        return permissionList;
//    }
//
//    @Operation(summary = "分页待办任务列表")
//    @GetMapping("/toDoPage")
//    public Result<PagedVO<FlowTaskVO>> toDoPage(FlowTask flowTask) {
//        List<String> permissionList = this.permissionList(UserContextHolder.userId());
//        flowTask.setPermissionList(permissionList);
//        return null;
////        return getDataTable(list);
//    }
////
////    /**
////     * 根据taskId查询代表任务
////     *
////     * @param taskId
////     * @return
////     */
////    @GetMapping("/getTaskById/{taskId}")
////    public Result<Task> getTaskById(@PathVariable("taskId") Long taskId) {
////        return Result.success(taskService.getById(taskId));
////    }
////
////    /**
////     * 查询跳转任意节点列表
////     */
////    @PreAuthorize("@ss.hasPermi('flow:execute:doneList')")
////    @GetMapping("/anyNodeList/{instanceId}")
////    public Result<List<Node>> anyNodeList(@PathVariable("instanceId") Long instanceId) {
////        Instance instance = insService.getById(instanceId);
////        List<Node> nodeList = nodeService.list(FlowEngine.newNode().setDefinitionId(instance.getDefinitionId()));
////        nodeList.removeIf(node -> NodeType.isGateWay(node.getNodeType()));
////        return Result.success(nodeList);
////    }
////
////    /**
////     * 处理非办理的流程交互类型
////     *
////     * @param warmFlowInteractiveTypeVo 要转办用户
////     * @return 是否成功
////     */
////    @PostMapping("/interactiveType")
////    public Result<Boolean> interactiveType(WarmFlowInteractiveTypeVo warmFlowInteractiveTypeVo) {
////        return Result.success(hhDefService.interactiveType(warmFlowInteractiveTypeVo));
////    }
//}
