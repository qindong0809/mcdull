package io.gitee.dqcer.mcdull.uac.provider.xcr.controller;

import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.vo.ProductionScheduleVO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.service.ProductionScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */
@RestController
@Tag(name = "生产进度表")
public class ProductionScheduleController {

    @Resource
    private ProductionScheduleService productionScheduleService;

    @Operation(summary = "分页")
    @PostMapping("/productionSchedule/queryPage")
    public Result<PagedVO<ProductionScheduleVO>> queryPage(@RequestBody @Valid ProductionScheduleQueryDTO dto) {
        return Result.success(productionScheduleService.queryPage(dto));
    }

    @Operation(summary = "添加")
    @PostMapping("/productionSchedule/add")
    public Result<Boolean> add(@RequestBody @Valid ProductionScheduleAddDTO dto) {
        productionScheduleService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping("/productionSchedule/update")
    public Result<Boolean> update(@RequestBody @Valid ProductionScheduleUpdateDTO dto) {
        productionScheduleService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "批量删除")
    @PostMapping("/productionSchedule/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> idList) {
        productionScheduleService.batchDelete(idList);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/productionSchedule/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable Integer id) {
        productionScheduleService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }

    @Operation(summary = "导出数据")
    @PostMapping(value = "/productionSchedule/record-export", produces = "application/octet-stream")
    public void exportData(@RequestBody @Valid ProductionScheduleQueryDTO dto) {
        productionScheduleService.exportData(dto);
    }
}
