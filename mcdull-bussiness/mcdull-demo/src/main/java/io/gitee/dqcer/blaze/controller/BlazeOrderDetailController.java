package io.gitee.dqcer.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailAddDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderDetailListVO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.blaze.service.IBlazeOrderDetailService;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@RestController
@Tag(name = "订单流水")
public class BlazeOrderDetailController extends BasicController {

    @Resource
    private IBlazeOrderDetailService blazeOrderDetailService;

    @Operation(summary = "分页")
    @PostMapping("/blazeOrderDetail/queryPage")
    public Result<PagedVO<BlazeOrderDetailVO>> queryPage(@RequestBody @Valid BlazeOrderDetailQueryDTO dto) {
        return Result.success(blazeOrderDetailService.queryPage(dto));
    }

    @Operation(summary = "订单列表（人才）")
    @PostMapping("/blazeOrderDetail/order-list")
    public Result<List<BlazeOrderDetailListVO>> getOderListByTalent() {
        return Result.success(blazeOrderDetailService.getOderListByTalent());
    }

    @Operation(summary = "订单列表（企业）")
    @PostMapping("/blazeOrderDetail/order-list-by-customer")
    public Result<List<BlazeOrderDetailListVO>> getOderListByCustomer() {
        return Result.success(blazeOrderDetailService.getOderListByCustomer());
    }

    @Operation(summary = "负责人")
    @PostMapping("/blazeOrderDetail/responsible-list")
    public Result<List<LabelValueVO<Integer, String>>> getResponsibleList() {
        return Result.success(blazeOrderDetailService.getResponsibleList());
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission(value = {"blaze:order_detail_talent:export", "blaze:order_detail_customer:export"}, mode = SaMode.OR)
    @PostMapping(value = "/blazeOrderDetail/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid BlazeOrderDetailQueryDTO dto) {
        this.setUnifySession(dto.getIsTalent());
        blazeOrderDetailService.exportData(dto);
    }

    private void setUnifySession(boolean isTalent) {
        UnifySession session = UserContextHolder.getSession();
        session.setPermissionCode(isTalent ? "blaze:order_detail_talent:export" : "blaze:order_detail_customer:export");
        UserContextHolder.setSession(session);
    }

    @Operation(summary = "添加")
    @SaCheckPermission(value = {"blaze:order_detail_talent:write", "blaze:order_detail_customer:write"}, mode = SaMode.OR)
    @PostMapping("/blazeOrderDetail/add")
    public Result<Boolean> add(@RequestBody @Valid BlazeOrderDetailAddDTO dto) {
        this.setUnifySession(dto.getIsTalent());
        blazeOrderDetailService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @SaCheckPermission(value = {"blaze:order_detail_talent:write", "blaze:order_detail_customer:write"}, mode = SaMode.OR)
    @PostMapping("/blazeOrderDetail/update")
    public Result<Boolean> update(@RequestBody @Valid BlazeOrderDetailUpdateDTO dto) {
        this.setUnifySession(blazeOrderDetailService.isTalent(dto.getId()));
        blazeOrderDetailService.update(dto);
        return Result.success(true);
    }


    @Operation(summary = "删除")
    @SaCheckPermission(value = {"blaze:order_detail_talent:write", "blaze:order_detail_customer:write"}, mode = SaMode.OR)
    @GetMapping("/blazeOrderDetail/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        this.setUnifySession(blazeOrderDetailService.isTalent(id));
        blazeOrderDetailService.delete(id);
        return Result.success(true);
    }
}
