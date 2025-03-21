package io.gitee.dqcer.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderAddDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.blaze.service.IBlazeOrderService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@RestController
@Tag(name = "订单合同")
public class BlazeOrderController extends BasicController {

    @Resource
    private IBlazeOrderService blazeOrderService;

    @Operation(summary = "分页")
    @PostMapping("/blazeOrder/queryPage")
    public Result<PagedVO<BlazeOrderVO>> queryPage(@RequestBody @Valid BlazeOrderQueryDTO dto) {
        return Result.success(blazeOrderService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("blaze:order:export")
    @PostMapping(value = "/blazeOrder/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid BlazeOrderQueryDTO dto) {
        blazeOrderService.exportData(dto);
    }

    @Operation(summary = "添加")
    @SaCheckPermission("blaze:order:write")
    @PostMapping("/blazeOrder/add")
    public Result<Boolean> add(@RequestBody @Valid BlazeOrderAddDTO dto) {
        blazeOrderService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @SaCheckPermission("blaze:order:write")
    @PostMapping("/blazeOrder/update")
    public Result<Boolean> update(@RequestBody @Valid BlazeOrderUpdateDTO dto) {
        blazeOrderService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @SaCheckPermission("blaze:order:write")
    @GetMapping("/blazeOrder/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        blazeOrderService.delete(id);
        return Result.success(true);
    }
}
