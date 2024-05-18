package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
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

    @Operation(summary = "分页列表")
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
    public Result<RoleMenuTreeVO> getTreeRoleId(@PathVariable Long roleId) {
        return Result.success(menuService.getTreeRoleId(roleId));
    }

    @Operation(summary = "批量删除")
    @GetMapping("/menu/batchDelete")
    @SaCheckPermission("system:menu:batchDelete")
    public Result<Boolean> batchDeleteMenu(@RequestParam("menuIdList") List<Long> menuIdList) {
        menuService.delete(menuIdList);
        return Result.success(true);
    }


//    @PutMapping("{id}/update")
//    public Result<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated MenuUpdateDTO dto){
//        return Result.success(menuService.update(id, dto));
//    }

//    /**
//     * 单个删除
//     *
//     * @param dto dto
//     * @return {@link Result<Integer>}
//     */
//    @DeleteMapping("{id}")
//    public Result<Boolean> delete(@PathVariable("id") Long id, @Validated ReasonDTO dto){
//        return Result.success(menuService.delete(id, dto));
//    }

//    @GetMapping("role-menu")
//    public Result<List<RoleMenuVO>> roleMenuList(){
//        return Result.success(menuService.roleMenuList());
//    }
//
//    @GetMapping("{roleId}/role-menu-ids")
//    public Result<List<Long>> roleMenuIdList(@PathVariable("roleId") Long roleId){
//        return Result.success(menuService.roleMenuIdList(roleId));
//    }

}
