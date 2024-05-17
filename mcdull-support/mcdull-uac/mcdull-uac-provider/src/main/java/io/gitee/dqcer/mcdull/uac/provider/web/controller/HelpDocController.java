package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocCatalogVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocDetailVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocViewRecordVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IHelpDocCatalogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IHelpDocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 *
 * @author dqcer
 */
@Tag(name = "帮助文档")
@RestController
public class HelpDocController extends BasicController {

    @Resource
    private IHelpDocService helpDocService;

    @Operation(summary = "【用户】-查看详情")
    @GetMapping("/helpDoc/user/view/{helpDocId}")
//    @RepeatSubmit
    public Result<HelpDocDetailVO> view(@PathVariable Long helpDocId) {
        return Result.success(helpDocService.view(helpDocId));
    }

    @Operation(summary = "【用户】-查询全部")
    @GetMapping("/helpDoc/user/queryAllHelpDocList")
//    @RepeatSubmit
    public Result<List<HelpDocVO>> queryAllHelpDocList() {
        return Result.success(helpDocService.queryAllHelpDocList());
    }


    @Operation(summary = "【用户】-查询 查看记录")
    @PostMapping("/helpDoc/user/queryViewRecord")
//    @RepeatSubmit
    public Result<PagedVO<HelpDocViewRecordVO>> queryViewRecord(@RequestBody @Valid HelpDocViewRecordQueryDTO dto) {
        return Result.success(helpDocService.queryViewRecord(dto));
    }

    @Operation(summary = "【管理】-分页查询")
    @PostMapping("/helpDoc/query")
    @SaCheckPermission("support:helpDoc:query")
    public Result<PagedVO<HelpDocVO>> query(@RequestBody @Valid HelpDocQueryDTO queryForm) {
        return Result.success(helpDocService.query(queryForm));
    }

    @Operation(summary = "【管理】-获取详情")
    @GetMapping("/helpDoc/getDetail/{helpDocId}")
    @SaCheckPermission("support:helpDoc:add")
    public Result<HelpDocDetailVO> getDetail(@PathVariable Long helpDocId) {
        return Result.success(helpDocService.getDetail(helpDocId));
    }

    @Operation(summary = "【管理】-添加")
    @PostMapping("/helpDoc/add")
//    @RepeatSubmit
    public Result<Boolean> add(@RequestBody @Valid HelpDocAddDTO addForm) {
         helpDocService.add(addForm);
         return Result.success(true);
    }

    @Operation(summary = "【管理】-更新")
    @PostMapping("/helpDoc/update")
//    @RepeatSubmit
    public Result<Boolean> update(@RequestBody @Valid HelpDocUpdateDTO updateForm) {
         helpDocService.update(updateForm);
        return Result.success(true);
    }

    @Operation(summary = "【管理】-删除")
    @GetMapping("/helpDoc/delete/{helpDocId}")
    public Result<Boolean> delete(@PathVariable Long helpDocId) {
        helpDocService.delete(helpDocId);
        return Result.success(true);
    }

    @Operation(summary = "【管理】-根据关联id查询")
    @GetMapping("/helpDoc/queryHelpDocByRelationId/{relationId}")
    public Result<List<HelpDocVO>> queryHelpDocByRelationId(@PathVariable Long relationId) {
        return Result.success(helpDocService.queryHelpDocByRelationId(relationId));
    }

}
