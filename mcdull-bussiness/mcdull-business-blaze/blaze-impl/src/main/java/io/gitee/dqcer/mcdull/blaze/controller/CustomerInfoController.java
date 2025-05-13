package io.gitee.dqcer.mcdull.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.blaze.domain.form.CustomerInfoAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.CustomerInfoQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.CustomerInfoUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.CustomerInfoVO;
import io.gitee.dqcer.mcdull.blaze.service.ICustomerInfoService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */
@RestController
@Tag(name = "企业端信息")
public class CustomerInfoController extends BlazeBasicController{

    @Resource
    private ICustomerInfoService customerInfoService;

    @Operation(summary = "分页")
    @PostMapping("/customerInfo/queryPage")
    public Result<PagedVO<CustomerInfoVO>> queryPage(@RequestBody @Valid CustomerInfoQueryDTO dto) {
        return Result.success(super.executeByPermission(null, PageUtil.empty(dto), dto,
                r -> customerInfoService.queryPage(r)));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("blaze:customer_info:export")
    @PostMapping(value = "/customerInfo/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid CustomerInfoQueryDTO dto) {
        super.executeByPermission(null, true, dto, r -> customerInfoService.exportData(r));
    }

    @Operation(summary = "全部数据")
    @PostMapping("/customerInfo/list")
    public Result<List<LabelValueVO<Integer, String>>> list() {
        return Result.success(customerInfoService.list());
    }

    @Operation(summary = "添加")
    @SaCheckPermission("blaze:customer_info:write")
    @PostMapping("/customerInfo/add")
    public Result<Boolean> add(@RequestBody @Valid CustomerInfoAddDTO dto) {
        customerInfoService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @SaCheckPermission("blaze:customer_info:write")
    @PostMapping("/customerInfo/update")
    public Result<Boolean> update(@RequestBody @Valid CustomerInfoUpdateDTO dto) {
        customerInfoService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "查询")
    @SaCheckPermission("blaze:customer_info:write")
    @PostMapping("/customerInfo/{id}")
    public Result<CustomerInfoVO> update(@PathVariable(value = "id") Integer id) {
        return Result.success(customerInfoService.detail(id));
    }


    @Operation(summary = "删除")
    @SaCheckPermission("blaze:customer_info:write")
    @GetMapping("/customerInfo/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        customerInfoService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }
}
