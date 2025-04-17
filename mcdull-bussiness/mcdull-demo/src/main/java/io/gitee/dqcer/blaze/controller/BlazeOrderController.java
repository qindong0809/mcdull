package io.gitee.dqcer.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderAddDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderSearchDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.blaze.service.IBlazeOrderService;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.dto.PkDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PostMapping(value = "/blazeOrder/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> add(@Valid BlazeOrderAddDTO dto,
                               @RequestPart(value = "file", required = false) List<MultipartFile> fileList) {
        dto.setContractTime(DateUtil.parseDate(dto.getContractTimeStr()));
        String startDateStr = dto.getStartDateStr();
        if (StrUtil.isNotBlank(startDateStr)) {
            dto.setStartDate(DateUtil.parseDate(startDateStr));
        }
        String endDateStr = dto.getEndDateStr();
        if (StrUtil.isNotBlank(endDateStr)) {
            dto.setEndDate(DateUtil.parseDate(endDateStr));
        }
        blazeOrderService.insert(dto, fileList);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @SaCheckPermission("blaze:order:write")
    @PostMapping(value = "/blazeOrder/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<Boolean> update(@Valid BlazeOrderUpdateDTO dto,
                                  @RequestPart(value = "file", required = false) List<MultipartFile> fileList) {
        dto.setContractTime(DateUtil.parseDate(dto.getContractTimeStr()));
        String startDateStr = dto.getStartDateStr();
        if (StrUtil.isNotBlank(startDateStr)) {
            dto.setStartDate(DateUtil.parseDate(startDateStr));
        }
        String endDateStr = dto.getEndDateStr();
        if (StrUtil.isNotBlank(endDateStr)) {
            dto.setEndDate(DateUtil.parseDate(endDateStr));
        }
        blazeOrderService.update(dto, fileList);
        return Result.success(true);
    }

    @Operation(summary = "审批")
    @SaCheckPermission("blaze:order:approve")
    @PostMapping("/blazeOrder/approve")
    public Result<Boolean> approve(@RequestBody @Valid ApproveDTO dto) {
        blazeOrderService.approve(dto);
        return Result.success(true);
    }

    @Operation(summary = "企业证书列表")
    @PostMapping("/blazeOrder/customer-cert")
    public Result<List<LabelValueVO<Integer, String>>> getCustomerCertListByOrderId(@RequestBody @Valid PkDTO pkDTO) {
        List<LabelValueVO<Integer, String>> list = blazeOrderService.getCustomerCertListByOrderId(pkDTO.getId());
        return Result.success(list);
    }

    @Operation(summary = "人才证书列表")
    @PostMapping("/blazeOrder/talent-cert-list")
    public Result<List<LabelValueVO<Integer, String>>> getTalentCertList(@RequestBody @Valid BlazeOrderSearchDTO pkDTO) {
        List<LabelValueVO<Integer, String>> list = blazeOrderService.getTalentCertList(pkDTO);
        CollUtil.sort(list, (o1, o2) -> NumberUtil.compare(o1.getValue(), o2.getValue()));
        return Result.success(list);
    }

    @Operation(summary = "企业证书列表")
    @PostMapping("/blazeOrder/custom-cert-list")
    public Result<List<LabelValueVO<Integer, String>>> getCustomCertList(@RequestBody @Valid BlazeOrderSearchDTO pkDTO) {
        List<LabelValueVO<Integer, String>> list = blazeOrderService.getCustomCertList(pkDTO);
        CollUtil.sort(list, (o1, o2) -> NumberUtil.compare(o1.getValue(), o2.getValue()));
        return Result.success(list);
    }

    @Operation(summary = "人才证书列表(根据企业证书匹配)")
    @PostMapping("/blazeOrder/talent-cert/{customerCertId}")
    public Result<List<LabelValueVO<Integer, String>>> getTalentCertListByOrderId(@RequestBody @Valid PkDTO pkDTO, @PathVariable(value = "customerCertId") Integer customerCertId) {
        List<LabelValueVO<Integer, String>> list = blazeOrderService.getTalentCertListByOrderId(customerCertId, pkDTO.getId());
        return Result.success(list);
    }


    @Operation(summary = "删除")
    @SaCheckPermission("blaze:order:write")
    @GetMapping("/blazeOrder/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        blazeOrderService.delete(id);
        return Result.success(true);
    }
}
