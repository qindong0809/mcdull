package io.gitee.dqcer.mcdull.uac.provider.web.controller;


import cn.hutool.core.convert.Convert;
//import com.warm.flow.core.entity.Definition;
//import com.warm.flow.core.service.DefService;
//import com.warm.flow.core.utils.page.Page;
//import com.warm.flow.orm.entity.FlowDefinition;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FlowDefinitionQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@Tag(name = "流程定义")
@RequestMapping("flow/definition")
public class FlowDefController extends BasicController {

//    @Resource
//    private DefService defService;
//
//    @Operation(summary = "分页查询流程定义列表")
//    @PostMapping("/list")
//    public Result<PagedVO<FlowDefinition>> list(@Valid @RequestBody FlowDefinitionQueryDTO queryDTO) {
//
//        Page<Definition> page = Page.pageOf(queryDTO.getPageNum(), queryDTO.getPageSize());
//        FlowDefinition flowDefinition = new FlowDefinition();
//        page = defService.orderByCreateTime().desc().page(flowDefinition, page);
//        PagedVO<FlowDefinition> vo = new PagedVO(page.getList(), Convert.toInt(page.getTotal()), page.getPageSize(), page.getPageNum());
//        return Result.success(vo);
//    }
//
//    /**
//     * 新增流程定义
//     */
////    @PreAuthorize("@ss.hasPermi('flow:definition:add')")
//    @PostMapping
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> add(@RequestBody FlowDefinition flowDefinition) {
//        return Result.success(defService.checkAndSave(flowDefinition));
//    }
}