package io.gitee.dqcer.mcdull.system.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FolderInsertDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FolderUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FolderInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FolderTreeInfoVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件夹控制器
 *
 * @author dqcer
 * @since 2024/11/29
 */
@Tag(name = "文件夹")
@RequestMapping("folder")
@RestController
public class FolderController {

    @Resource
    private IFolderService folderService;

    @Operation(summary = "查询全部列表")
    @GetMapping("list-all")
    public Result<List<FolderInfoVO>> getAll() {
        return Result.success(folderService.getAll());
    }

    @Operation(summary = "Add")
    @PostMapping("insert")
    public Result<Integer> insert(@RequestBody @Validated FolderInsertDTO dto){
        return Result.success(folderService.insert(dto, true));
    }

    @Operation(summary = "Update")
    @PostMapping("update")
//    @SaCheckPermission("system:department:write")
    public Result<Boolean> updateDepartment(@Valid @RequestBody FolderUpdateDTO dto) {
        return Result.success(folderService.update(dto.getId(), dto));
    }

    @Operation(summary = "Delete ")
    @DeleteMapping("delete/{folderId}")
//    @SaCheckPermission("system:department:write")
    public Result<Boolean> deleteFolder(@PathVariable(value = "folderId") Integer folderId) {
        return Result.success(folderService.delete(folderId));
    }

    @Operation(summary = "查询树形列表")
    @GetMapping("treeList")
    public Result<List<FolderTreeInfoVO>> getTree() {
        return Result.success(folderService.getTree());
    }
}
