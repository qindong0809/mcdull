package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleMenuTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Tag(name = "菜单管理")
@RestController
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Operation(summary = "列表")
    @GetMapping("/menu/list")
    public Result<List<MenuVO>> list(@Validated MenuListDTO dto) {
        return Result.success(menuService.list(dto));
    }

    @Operation(summary = "查询菜单树")
    @GetMapping("/menu/tree")
    public Result<List<MenuTreeVO>> queryMenuTree(@RequestParam("onlyMenu") Boolean onlyMenu) {
        return Result.success(menuService.queryMenuTree(onlyMenu));
    }

    @Operation(summary = "添加菜单")
    @PostMapping("/menu/add")
    @SaCheckPermission("system:menu:add")
    public Result<Boolean> addMenu(@RequestBody @Valid MenuAddDTO dto) {
        menuService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新菜单")
    @PostMapping("/menu/update")
    @SaCheckPermission("system:menu:update")
    public Result<Boolean> updateMenu(@RequestBody @Valid MenuUpdateDTO dto) {
        menuService.update(dto);
        return Result.success(true);
    }
    @Operation(summary = "获取角色关联菜单权限")
    @GetMapping("/menu/{roleId}/tree")
    public Result<RoleMenuTreeVO> getTreeRoleId(@PathVariable Integer roleId) {
        return Result.success(menuService.getTreeRoleId(roleId));
    }

    @Operation(summary = "批量删除")
    @GetMapping("/menu/batchDelete")
    @SaCheckPermission("system:menu:batchDelete")
    public Result<Boolean> batchDeleteMenu(@RequestParam("menuIdList") List<Integer> menuIdList) {
        menuService.delete(menuIdList);
        return Result.success(true);
    }

    @Operation(summary = "导出数据")
//    @SaCheckPermission("support:form:record:export")
    @PostMapping(value = "/menu/record-export", produces = "application/octet-stream")
    public void exportData(@Validated MenuListDTO dto) {
        menuService.exportData(dto);
    }

    @Operation(summary = "下拉选项")
    @GetMapping("/menu/dropdown-options")
    public Result<List<LabelValueVO<String, String>>> getDropdownOptions() {
        return Result.success(menuService.getDropdownOptions());
    }
}
