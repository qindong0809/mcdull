//package io.gitee.dqcer.mcdull.system.provider.web.controller.workflow;
//
//
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.map.MapUtil;
//import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
//import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
//import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
//import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
//import io.gitee.dqcer.mcdull.system.provider.model.dto.FlowDefinitionQueryDTO;
//import io.doc.v3.oas.annotations.Operation;
//import io.doc.v3.oas.annotations.tags.Tag;
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import org.dromara.warm.flow.core.chart.BetweenChart;
//import org.dromara.warm.flow.core.chart.FlowChart;
//import org.dromara.warm.flow.core.entity.Definition;
//import org.dromara.warm.flow.core.service.ChartService;
//import org.dromara.warm.flow.core.service.DefService;
//import org.dromara.warm.flow.core.utils.page.Page;
//import org.dromara.warm.flow.orm.entity.FlowDefinition;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.awt.*;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@Tag(name = "流程定义")
//@RequestMapping("flow/definition")
//public class FlowDefController extends BasicController {
//
//    @Resource
//    private DefService defService;
//
//    @Resource
//    private ChartService chartService;
//
//    @Operation(summary = "分页查询流程定义列表")
//    @PostMapping("/list")
//    public Result<PagedVO<Definition>> list(@Valid @RequestBody FlowDefinitionQueryDTO queryDTO) {
//        Page<Definition> page = Page.pageOf(queryDTO.getPageNum(), queryDTO.getPageSize());
//        Definition flowDefinition = new FlowDefinition();
//        flowDefinition.setDelFlag("0");
//        page = defService.orderByCreateTime().desc().page(flowDefinition, page);
//        PagedVO<Definition> vo = new PagedVO<>(page.getList(), Convert.toInt(page.getTotal()), page.getPageSize(), page.getPageNum());
//        return Result.success(vo);
//    }
//
//    @Operation(summary = "获取流程定义详细信息")
//    @GetMapping(value = "/{id}")
//    public Result<Definition> getInfo(@PathVariable("id") Long id) {
//        return Result.success(defService.getById(id));
//    }
//
//
//    @Operation(summary = "新增流程定义")
//    @PostMapping
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> add(@RequestBody FlowDefinition flowDefinition) {
//        return Result.success(defService.checkAndSave(flowDefinition));
//    }
//
//
//    @Operation(summary = "发布流程定义")
//    @GetMapping("/publish/{id}")
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> publish(@PathVariable("id") Long id) {
//        return Result.success(defService.publish(id));
//    }
//
//
//    @Operation(summary = "取消发布流程定义")
//    @GetMapping("/unPublish/{id}")
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Void> unPublish(@PathVariable("id") Long id) {
//        defService.unPublish(id);
//        return Result.success();
//    }
//
//    @Operation(summary = "修改流程定义")
//    @PutMapping
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> edit(@RequestBody FlowDefinition flowDefinition) {
//        return Result.success(defService.updateById(flowDefinition));
//    }
//
//
//    @Operation(summary = "删除流程定义")
//    @DeleteMapping("/{ids}")
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> remove(@PathVariable List<Long> ids) {
//        return Result.success(defService.removeDef(ids));
//    }
//
//    @Operation(summary = "复制流程定义")
//    @GetMapping("/copyDef/{id}")
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> copyDef(@PathVariable("id") Long id) {
//        return Result.success(defService.copyDef(id));
//    }
//
//    @Operation(summary = "导入流程定义")
//    @PostMapping("/importDefinition")
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Void> importDefinition(MultipartFile file) throws Exception {
//        defService.importIs(file.getInputStream());
//        return Result.success();
//    }
//
//    @Operation(summary = "导出流程定义")
//    @PostMapping("/exportDefinition/{id}")
//    public void exportDefinition(@PathVariable("id") Long id) throws IOException {
//        // 要导出的字符串
//        String content = defService.exportJson(id);
//        HttpServletResponse response = super.getResponse();
//        ServletUtil.setDownloadFileHeader(response, "exported_string.txt");
//        response.getOutputStream().write(content.getBytes(StandardCharsets.UTF_8));
//    }
//
//    @Operation(summary = "查询流程图")
//    @GetMapping("/chartDef/{definitionId}")
//    public Result<String> chartDef(@PathVariable("definitionId") Long definitionId) {
//        return Result.success(chartService.chartDef(definitionId));
//    }
//
//    @Operation(summary = "查询流程图")
//    @GetMapping("/flowChart/{instanceId}")
//    public Result<String> flowChart(@PathVariable("instanceId") Long instanceId) {
//        return Result.success(chartService.chartIns(instanceId, (flowChartChain) -> {
//            List<FlowChart> flowChartList = flowChartChain.getFlowChartList();
//            flowChartList.forEach(flowChart -> {
//                if (flowChart instanceof BetweenChart) {
//                    BetweenChart betweenChart = (BetweenChart) flowChart;
//                    Map<String, Object> extMap = betweenChart.getNodeJson().getExtMap();
//                    // 给节点顶部增加文字说明
////                    betweenChart.topText("办理时间: 2025-02-08 12:12:12", Color.red);
//                    if (MapUtil.isNotEmpty(extMap)) {
//                        for(Map.Entry<String, Object> entry : extMap.entrySet()){
//                            // 给节点中追加文字
//                            betweenChart.addText(entry.getKey() + ":", Color.red);
//                            betweenChart.addText((String) entry.getValue(), Color.red);
//                        }
//                    }
//                }
//            });
//        }));
//    }
//
//    @Operation(summary = "激活流程")
//    @GetMapping("/active/{definitionId}")
//    public Result<Boolean> active(@PathVariable("definitionId") Long definitionId) {
//        return Result.success(defService.active(definitionId));
//    }
//
//
//    @Operation(summary = "挂起流程")
//    @GetMapping("/unActive/{definitionId}")
//    public Result<Boolean> unActive(@PathVariable("definitionId") Long definitionId) {
//        return Result.success(defService.unActive(definitionId));
//    }
//}