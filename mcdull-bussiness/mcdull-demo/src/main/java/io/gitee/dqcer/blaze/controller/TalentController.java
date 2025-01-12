package io.gitee.dqcer.blaze.controller;

import io.gitee.dqcer.blaze.domain.form.TalentAddDTO;
import io.gitee.dqcer.blaze.domain.form.TalentQueryDTO;
import io.gitee.dqcer.blaze.domain.form.TalentUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.TalentVO;
import io.gitee.dqcer.blaze.service.ITalentService;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */
@RestController
@Tag(name = "人才表")
public class TalentController extends BasicController {

    @Resource
    private ITalentService talentService;

    @Operation(summary = "分页")
    @PostMapping("/talent/queryPage")
    public Result<PagedVO<TalentVO>> queryPage(@RequestBody @Valid TalentQueryDTO dto) {
        return Result.success(talentService.queryPage(dto));
    }

    @Operation(summary = "全部数据")
    @PostMapping("/talent/list")
    public Result<List<LabelValueVO<Integer, String>>> list() {
        return Result.success(talentService.list());
    }

    @Operation(summary = "添加")
    @PostMapping(value = "/talent/add")
    public Result<Boolean> add(@RequestBody @Valid TalentAddDTO dto) {
        talentService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping(value ="/talent/update")
    public Result<Boolean> update(@RequestBody @Valid TalentUpdateDTO dto) {
        talentService.update(dto);
        return Result.success(true);
    }


    @Operation(summary = "删除")
    @GetMapping("/talent/delete/{id}")
    public Result<Boolean> batchDelete(@PathVariable Integer id) {
        talentService.delete(id);
        return Result.success(true);
    }
}
