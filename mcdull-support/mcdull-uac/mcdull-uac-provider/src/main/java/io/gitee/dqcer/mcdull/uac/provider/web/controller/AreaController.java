package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.AreaVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@RestController
@Tag(name = "行政区域")
public class AreaController extends BasicController {

    @Resource
    private IAreaService areaService;

    @Operation(summary = "分页")
    @PostMapping("/area/queryPage")
    public Result<PagedVO<AreaVO>> queryPage(@RequestBody @Valid AreaQueryDTO dto) {
        return Result.success(areaService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:area:export")
    @PostMapping(value = "/system/area/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid AreaQueryDTO dto) {
        areaService.exportData(dto);
    }


    @Operation(summary = "省份")
    @PostMapping("/area/provinceList")
    public Result<List<LabelValueVO<String, String>>> provinceList() {
        return Result.success(areaService.provinceList());
    }

    @Operation(summary = "城市")
    @GetMapping("/area/{provinceCode}/city-list")
    public Result<List<LabelValueVO<String, String>>> cityList(@PathVariable(value = "provinceCode") String provinceCode) {
        return Result.success(areaService.cityList(provinceCode));
    }



}