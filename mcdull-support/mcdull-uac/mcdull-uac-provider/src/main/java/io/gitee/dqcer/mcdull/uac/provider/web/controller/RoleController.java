package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Tag(name = "Role API")
@RestController
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @GetMapping("/role/list")
    public Result<PagedVO<RoleVO>> listByPage(@Validated RolePageDTO dto) {
        return Result.success(roleService.listByPage(dto));
    }

    @GetMapping("role/list-all")
    public Result<List<RoleVO>> getListAll() {
        return Result.success(roleService.all());
    }

    @Operation(summary = "添加角色 @author 卓大")
    @PostMapping("/role/add")
    @SaCheckPermission("system:role:add")
    public Result<Boolean> addRole(@Valid @RequestBody RoleAddDTO dto) {
        roleService.insert(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除角色 @author 卓大")
    @GetMapping("/role/delete/{roleId}")
    @SaCheckPermission("system:role:delete")
    public Result<Boolean> deleteRole(@PathVariable Long roleId) {
        roleService.delete(roleId);
        return Result.success(true);
    }

    @Operation(summary = "更新角色")
    @PostMapping("/role/update")
    @SaCheckPermission("system:role:update")
    public Result<Boolean> updateRole(@Valid @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(dto);
        return Result.success(true);
    }

    @Operation(summary = "获取角色数据 @author 卓大")
    @GetMapping("/role/get/{roleId}")
    public Result<RoleVO> getRole(@PathVariable("roleId") Long roleId) {
        return Result.success(roleService.detail(roleId));
    }


    @PutMapping("{id}/permission")
    public Result<Boolean> insertPermission(@PathVariable("id") Long id, @RequestBody RolePermissionInsertDTO dto) {
        return Result.success(roleService.insertPermission(id, dto));
    }

}
