package io.gitee.dqcer.mcdull.system.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocCatalogAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocCatalogUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.HelpDocCatalogVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IHelpDocCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;


/**
 *
 * @author dqcer
 */
@Tag(name = "帮助文档目录")
@RestController
public class HelpDocCatalogController extends BasicController {

    @Resource
    private IHelpDocCatalogService helpDocCatalogService;

    @Operation(summary = "获取全部")
    @GetMapping("/support/helpDoc/helpDocCatalog/getAll")
    public Result<List<HelpDocCatalogVO>> getAll() {
        return Result.success(helpDocCatalogService.getAll());
    }

    @Operation(summary = "添加")
    @PostMapping("/support/helpDoc/helpDocCatalog/add")
    public Result<Boolean> addHelpDocCatalog(@RequestBody @Valid HelpDocCatalogAddDTO helpDocCatalogAddDTO) {
        helpDocCatalogService.add(helpDocCatalogAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping("/support/helpDoc/helpDocCatalog/update")
    public Result<Boolean> updateHelpDocCatalog(@RequestBody @Valid HelpDocCatalogUpdateDTO helpDocCatalogUpdateForm) {
        helpDocCatalogService.update(helpDocCatalogUpdateForm);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/support/helpDoc/helpDocCatalog/delete/{helpDocCatalogId}")
    public Result<Boolean> deleteHelpDocCatalog(@PathVariable(value = "helpDocCatalogId") Integer helpDocCatalogId) {
        helpDocCatalogService.delete(helpDocCatalogId);
        return Result.success(true);
    }

}
