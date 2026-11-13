package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.RoleDataScopeUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DataScopeAndViewTypeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleDataScopeVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleDataScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 *
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Tag(name = "角色数据权限")
@RestController
public class RoleDataScopeController {

    @Resource
    private IRoleDataScopeService roleDataScopeService;

    @Operation(summary = "获取某角色所设置的数据范围")
    @GetMapping("/role/dataScope/getRoleDataScopeList/{roleId}")
    public Result<List<RoleDataScopeVO>> dataScopeListByRole(@PathVariable(value = "roleId") Integer roleId) {
        return Result.success(roleDataScopeService.getListByRole(roleId));
    }

    @Operation(summary = "批量设置某角色数据范围")
    @PostMapping("/role/dataScope/updateRoleDataScopeList")
    @SaCheckEL("stp.checkPermission('system:role:write')")
    public Result<Boolean> updateRoleDataScopeList(@RequestBody @Valid RoleDataScopeUpdateDTO dto) {
        roleDataScopeService.updateByRoleId(dto);
        return Result.success(true);
    }

    @Operation(summary = "获取当前系统所配置的所有数据范围")
    @GetMapping("/dataScope/list")
    public Result<List<DataScopeAndViewTypeVO>> dataScopeList() {
        return Result.success(roleDataScopeService.dataScopeList());
    }
}
