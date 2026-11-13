package io.gitee.dqcer.mcdull.workflow.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowTaskDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowHisTaskVO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowTaskVO;
import io.gitee.dqcer.mcdull.workflow.web.service.IExecuteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.dromara.warm.flow.orm.entity.FlowHisTask;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Flow execute")
@RestController
@RequestMapping("/workflow/execute")
public class ExecuteController extends BasicController {

    @Resource
    private IExecuteService executeService;

    @Operation(summary = "待办任务列表")
    @PostMapping("/toDoPage")
    public Result<PagedVO<FlowTaskVO>> toDoPage(@RequestBody FlowTaskDTO flowTask) {
        return Result.success(executeService.toDoPage(flowTask));
    }

    @Operation(summary = "抄送任务列表")
    @PostMapping("/copyPage")
    public Result<PagedVO<FlowHisTaskVO>> copyPage(@RequestBody FlowTaskDTO flowTask) {
        return Result.success(executeService.copyPage(flowTask));
    }

}
