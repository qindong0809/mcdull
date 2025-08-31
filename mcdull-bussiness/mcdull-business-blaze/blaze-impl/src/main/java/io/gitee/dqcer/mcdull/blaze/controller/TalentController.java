package io.gitee.dqcer.mcdull.blaze.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.TalentVO;
import io.gitee.dqcer.mcdull.blaze.service.ITalentService;
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
 * @since 2025-01-10 19:52:20
 */
@RestController
@Tag(name = "人才表")
public class TalentController extends BlazeBasicController {

    @Resource
    private ITalentService talentService;

    @Operation(summary = "分页")
    @PostMapping("/talent/queryPage")
    public Result<PagedVO<TalentVO>> queryPage(@RequestBody @Valid TalentQueryDTO dto) {
        return Result.success(super.executeByPermission(null, PageUtil.empty(dto), dto, r -> talentService.queryPage(r)));
    }

    @Operation(summary = "导出数据")
    @SaCheckEL("stp.checkPermission('blaze:talent:export')")
    @PostMapping(value = "/talent/list/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid TalentQueryDTO dto) {
        super.executeByPermission(null, true, dto, r -> talentService.exportData(r));
    }

    @Operation(summary = "全部数据")
    @PostMapping("/talent/list")
    public Result<List<LabelValueVO<Integer, String>>> list() {
        return Result.success(talentService.list());
    }

    @Operation(summary = "添加")
    @SaCheckEL("stp.checkPermission('blaze:talent:write')")
    @PostMapping(value = "/talent/add")
    public Result<Boolean> add(@RequestBody @Valid TalentAddDTO dto) {
        talentService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @SaCheckEL("stp.checkPermission('blaze:talent:write')")
    @PostMapping(value ="/talent/update")
    public Result<Boolean> update(@RequestBody @Valid TalentUpdateDTO dto) {
        talentService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "详情")
    @SaCheckEL("stp.checkPermission('blaze:talent:write')")
    @GetMapping(value ="/talent/{id}")
    public Result<TalentVO> update(@PathVariable(value = "id") Integer id) {
        return Result.success(talentService.detail(id));
    }

    @Operation(summary = "删除")
    @SaCheckEL("stp.checkPermission('blaze:talent:write')")
    @GetMapping("/talent/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable(value = "id") Integer id) {
        talentService.delete(id);
        return Result.success(true);
    }
}
