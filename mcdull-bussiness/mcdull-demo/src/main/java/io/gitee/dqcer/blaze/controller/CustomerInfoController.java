package io.gitee.dqcer.blaze.controller;

import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoAddDTO;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CustomerInfoVO;
import io.gitee.dqcer.blaze.service.ICustomerInfoService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */
@RestController
@Tag(name = "企业信息")
public class CustomerInfoController {

    @Resource
    private ICustomerInfoService customerInfoService;

    @Operation(summary = "分页")
    @PostMapping("/customerInfo/queryPage")
    public Result<PagedVO<CustomerInfoVO>> queryPage(@RequestBody @Valid CustomerInfoQueryDTO dto) {
        return Result.success(customerInfoService.queryPage(dto));
    }

    @Operation(summary = "添加")
    @PostMapping("/customerInfo/add")
    public Result<Boolean> add(@RequestBody @Valid CustomerInfoAddDTO dto) {
        customerInfoService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping("/customerInfo/update")
    public Result<Boolean> update(@RequestBody @Valid CustomerInfoUpdateDTO dto) {
        customerInfoService.update(dto);
        return Result.success(true);
    }


    @Operation(summary = "删除")
    @GetMapping("/customerInfo/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable Integer id) {
        customerInfoService.batchDelete(ListUtil.of(id));
        return Result.success(true);
    }
}
