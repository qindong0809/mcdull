package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.SerialNumberVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberRecordService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "编号规则")
@RequestMapping
public class SerialNumberController {

    @Resource
    private ISerialNumberService serialNumberService;

    @Resource
    private ISerialNumberRecordService serialNumberRecordService;

    @Operation(summary = "生成单号")
    @PostMapping("/serialNumber/generate")
    @SaCheckEL("stp.checkPermission('support:serialNumber:generate')")
    public Result<List<String>> generate(@RequestBody @Valid SerialNumberGenerateDTO dto) {
        return Result.success(serialNumberService.generate(dto));
    }

    @Operation(summary = "全部数据")
    @GetMapping("/serialNumber/all")
    public Result<List<SerialNumberVO>> getAll() {
        return Result.success(serialNumberService.getAll());
    }

    @Operation(summary = "获取生成记录")
    @PostMapping("/serialNumber/queryRecord")
    @SaCheckEL("stp.checkPermission('support:serialNumber:record')")
    public Result<PagedVO<SerialNumberRecordEntity>> queryRecord(@RequestBody @Valid
                                                                     SerialNumberRecordQueryDTO queryForm) {
        return Result.success(serialNumberRecordService.query(queryForm));
    }
}
