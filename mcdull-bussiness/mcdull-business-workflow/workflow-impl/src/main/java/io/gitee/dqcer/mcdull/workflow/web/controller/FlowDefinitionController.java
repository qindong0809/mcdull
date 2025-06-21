package io.gitee.dqcer.mcdull.workflow.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowDefinitionDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowDefinitionVO;
import io.gitee.dqcer.mcdull.workflow.web.service.IFlowDefinitionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Flow definition")
@RestController
@RequestMapping("/workflow/definition")
public class FlowDefinitionController extends BasicController {
    @Resource
    private IFlowDefinitionService flowDefinitionService;

    @GetMapping("list")
    public Result<PagedVO<FlowDefinitionVO>> getList(@Valid FlowDefinitionDTO dto) {
        return Result.success(flowDefinitionService.getList(dto));
    }
}
