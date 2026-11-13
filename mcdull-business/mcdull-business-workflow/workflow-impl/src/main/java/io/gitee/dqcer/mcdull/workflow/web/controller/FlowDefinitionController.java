package io.gitee.dqcer.mcdull.workflow.web.controller;

import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowDefinitionDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowDefinitionVO;
import io.gitee.dqcer.mcdull.workflow.web.service.IFlowDefinitionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.dromara.warm.flow.core.entity.Definition;
import org.dromara.warm.flow.core.service.DefService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "Flow definition")
@RestController
@RequestMapping("/workflow/definition")
public class FlowDefinitionController extends BasicController {

    @Resource
    private IFlowDefinitionService flowDefinitionService;

    @Resource
    private DefService defService;

    @PostMapping("list")
    public Result<PagedVO<FlowDefinitionVO>> getList(@RequestBody @Valid FlowDefinitionDTO dto) {
        return Result.success(flowDefinitionService.getList(dto));
    }

    @GetMapping(value = "/{id}")
    public Result<FlowDefinitionVO> getDetail(@PathVariable("id") Long id) {
        return Result.success(flowDefinitionService.getDetail(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody FlowDefinitionDTO flowDefinition) {
        return Result.success(flowDefinitionService.add(flowDefinition));
    }

    @GetMapping("/publish/{id}")
    public Result<Boolean> publish(@PathVariable("id") Long id) {
        return Result.success(flowDefinitionService.publish(id));
    }

    @GetMapping("/unPublish/{id}")
    public Result<Void> unPublish(@PathVariable("id") Long id) {
        flowDefinitionService.unPublish(id);
        return Result.success();
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody FlowDefinitionDTO flowDefinition) {
        return Result.success(flowDefinitionService.updateById(flowDefinition));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable("id") Long id) {
        List<Long> ids = List.of(id);
        return Result.success(flowDefinitionService.removeDef(ids));
    }

    @GetMapping("/copyDef/{id}")
    public Result<Boolean> copyDef(@PathVariable("id") Long id) {
        return Result.success(flowDefinitionService.copyDef(id));
    }

    @PostMapping("/importDefinition")
    public Result<Void> importDefinition(@RequestParam("file") MultipartFile file) {
        try {
            flowDefinitionService.importIs(file.getInputStream());
        } catch (Exception e) {
            LogHelp.error(log, "importDefinition error", e);
            return Result.error(super.dynamicLocaleMessageSource.getMessage(I18nConstants.DATA_NOT_EXIST));
        }
        return Result.success();
    }

    @PostMapping("/exportDefinition/{id}")
    public void exportDefinition(@PathVariable("id") Long id) {
        // 要导出的字符串
        String content = defService.exportJson(id);
        Definition definition = defService.getById(id);
        ServletUtil.download(definition.getFlowName() + ".json", content.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/active/{definitionId}")
    public Result<Boolean> active(@PathVariable("definitionId") Long definitionId) {
        return Result.success(defService.active(definitionId));
    }

    @GetMapping("/unActive/{definitionId}")
    public Result<Boolean> unActive(@PathVariable("definitionId") Long definitionId) {
        return Result.success(defService.unActive(definitionId));
    }


}
