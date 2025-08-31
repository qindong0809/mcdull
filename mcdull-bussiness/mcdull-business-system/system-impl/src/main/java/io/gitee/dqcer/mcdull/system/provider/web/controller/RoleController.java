package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 角色控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Tag(name = "角色管理")
@RestController
public class RoleController {

    @Resource
    private IRoleService roleService;

    @Resource
    private IUserService userService;


    @Operation(summary = "获取所有角色")
    @GetMapping("role/list-all")
    public Result<List<RoleVO>> getListAll() {
        return Result.success(roleService.all());
    }

    @Operation(summary = "添加角色")
    @PostMapping("/role/add")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> addRole(@Valid @RequestBody RoleAddDTO dto) {
        roleService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除角色")
    @GetMapping("/role/delete/{roleId}")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> deleteRole(@PathVariable(value = "roleId") Integer roleId) {
        roleService.delete(roleId);
        return Result.success(true);
    }

    @Operation(summary = "更新角色")
    @PostMapping("/role/update")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> updateRole(@Valid @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(dto);
        return Result.success(true);
    }

    @Operation(summary = "获取角色数据")
    @GetMapping("/role/get/{roleId}")
    public Result<RoleVO> getRole(@PathVariable("roleId") Integer roleId) {
        return Result.success(roleService.detail(roleId));
    }

    @Operation(summary = "更新角色权限")
    @PostMapping("/role/menu/updateRoleMenu")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> updateRoleMenu(@Valid @RequestBody RoleMenuUpdateDTO dto) {
        roleService.updateRoleMenu(dto);
        return Result.success(true);
    }

    @Operation(summary = "添加角色权限")
    @PutMapping("{id}/permission")
    public Result<Boolean> insertPermission(@PathVariable("id") Integer id, @RequestBody RolePermissionInsertDTO dto) {
        return Result.success(roleService.insertPermission(id, dto));
    }

    @Operation(summary = "从角色成员列表中批量移除员工")
    @PostMapping("/role/user/batch-remove-user")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> batchRemoveEmployee(@Valid @RequestBody RoleEmployeeUpdateDTO dto) {
        roleService.batchRemoveRoleEmployee(dto);
        return Result.success(true);
    }

    @Operation(summary = "从角色成员列表中移除员工")
    @GetMapping("/role/user/remove-user")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> removeEmployee(Integer userId, Integer roleId) {
        RoleEmployeeUpdateDTO dto = new RoleEmployeeUpdateDTO();
        dto.setEmployeeIdList(ListUtil.of(userId));
        dto.setRoleId(roleId);
        roleService.batchRemoveRoleEmployee(dto);
        return Result.success(true);
    }

    @Operation(summary = "根据角色查询用户分页列表")
    @GetMapping("role/{roleId}/user-list")
    public Result<PagedVO<UserVO>> listByPage(@PathVariable("roleId") Integer roleId,
                                              @Validated UserListDTO dto) {
        return Result.success(userService.pageByRoleId(roleId, dto));
    }

}
