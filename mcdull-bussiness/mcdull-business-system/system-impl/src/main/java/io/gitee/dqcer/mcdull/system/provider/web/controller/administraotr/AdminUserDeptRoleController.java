package io.gitee.dqcer.mcdull.system.provider.web.controller.administraotr;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Dict;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.DeptSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.RoleSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.UserSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminRoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.administrator.AdminRoleDataScopeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.*;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminDeptService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminRoleService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(GlobalConstant.ADMINISTRATOR_PATH)
@Tag(name = "Administrator")
public class AdminUserDeptRoleController extends BasicController {

    @Resource
    private IAdminDeptService adminDeptService;
    @Resource
    private IAdminUserService adminUserService;
    @Resource
    private IAdminRoleService adminRoleService;

    @Operation(summary = "Dept Tree", description = "获取部门信息")
    @GetMapping("system/dept/dict/tree")
    public Result<List<AdminDeptTreeVO>> getDeptTree() {
        return Result.success(ListUtil.of(adminDeptService.getDeptTree()));
    }

    @Operation(summary = "Dept Tree", description = "获取部门list信息")
    @GetMapping("system/dept/tree")
    public Result<List<AdminDeptListTreeVO>> getDeptListTree() {
        return Result.success(ListUtil.of(adminDeptService.getDeptListTree()));
    }

    @Operation(summary = "save dept", description = "save dept")
    @PostMapping("system/dept")
    public Result<Integer> saveDept(@Valid @RequestBody DeptSaveDTO dto) {
        return Result.success(adminDeptService.saveDept(dto));
    }

    @Operation(summary = "delete dept", description = "delete dept")
    @DeleteMapping("system/dept")
    public Result<Boolean> deleteDept(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");
        return Result.success(adminDeptService.deleteDept(ids));
    }

    @Operation(summary = "get dept", description = "get dept")
    @GetMapping("system/dept/{id}")
    public Result<AdminDeptInfoVO> getDept(@PathVariable("id") Integer id) {
        return Result.success(adminDeptService.get(id));
    }

    @Operation(summary = "edit dept", description = "edit dept")
    @PutMapping("system/dept/{id}")
    public Result<Boolean> editDept(@PathVariable("id") Integer id, @Valid @RequestBody DeptSaveDTO dto) {
        return Result.success(adminDeptService.editDept(id, dto));
    }

    @Operation(summary = "export dept", description = "export dept")
    @GetMapping("system/dept/export")
    public void exportDept() {
        adminDeptService.exportDept();
    }

    @Operation(summary = "User Tree", description = "获取用户信息")
    @GetMapping("system/user")
    public Result<Dict> getUserList(@RequestParam(name = "page") Integer pageNum,
                                    @RequestParam(name = "size") Integer pageSize,
                                    @RequestParam(name = "deptId", required = false) Integer deptId,
                                    @RequestParam(name = "roleId", required = false) Integer roleId,
                                    @RequestParam(name = "status", required = false) Integer status,
                                    @RequestParam(name = "createTime", required = false) String createTime,
                                    @RequestParam(name = "description", required = false) String description) {
        Integer notInRoleId = roleId;
        PagedVO<AdminUserVO> pagedVO = adminUserService.getUserList(pageNum, pageSize, deptId, status, createTime, description, null, notInRoleId);
        Dict dict = Dict.create().set("list", pagedVO.getList()).set("total", pagedVO.getTotal());
        return Result.success(dict);
    }

    @Operation(summary = "get user", description = "get user")
    @GetMapping("system/user/{id}")
    public Result<AdminUserSimpleVO> getUser(@PathVariable("id") Integer id) {
        return Result.success(adminUserService.getUser(id));
    }

    @Operation(summary = "save user", description = "save user")
    @PostMapping("system/user")
    public Result<Integer> saveUser(@Valid @RequestBody UserSaveDTO dto) {
        return Result.success(adminUserService.saveUser(dto));
    }

    @Operation(summary = "edit user", description = "edit user")
    @PutMapping("system/user/{id}")
    public Result<Boolean> editUser(@PathVariable("id") Integer id, @Valid @RequestBody UserSaveDTO dto) {
        return Result.success(adminUserService.editUser(id, dto));
    }

    @Operation(summary = "delete user", description = "delete user")
    @DeleteMapping("system/user")
    public Result<Boolean> deleteUser(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");
        return Result.success(adminUserService.deleteUser(ids));
    }

    @Operation(summary = "edit user role", description = "edit user role")
    @PatchMapping("system/user/{id}/role")
    public Result<Boolean> editUserRole(@PathVariable("id") Integer id, @RequestBody Map<String, List<Integer>> request) {
        List<Integer> roleIds = request.get("roleIds");
        return Result.success(adminUserService.editUserRole(id, roleIds));
    }

    //*******************************************************role*******************************************************//

    @Operation(summary = "get role", description = "get role")
    @GetMapping("system/role/dict")
    public Result<List<Dict>> getRole() {
        List<AdminRoleEntity> list = adminRoleService.getList();
        List<Dict> dictList = new ArrayList<>();
        for (AdminRoleEntity entity : list) {
            Dict dict = Dict.create().set("label", entity.getName()).set("value", entity.getId());
            dictList.add(dict);
        }
        return Result.success(dictList);
    }

    @Operation(summary = "role list", description = "获取角色信息")
    @GetMapping("system/role/list")
    public Result<List<AdminRoleVO>> getRoleList() {
        return Result.success(adminRoleService.getRoleList());
    }

    @Operation(summary = "role tree", description = "获取角色权限信息")
    @GetMapping("system/role/permission/tree")
    public Result<List<RolePermissionVO>> getRoleTreeList() {
        return Result.success(adminRoleService.getRoleTreeList());
    }

    @Operation(summary = "role ", description = "获取角色权限信息")
    @GetMapping("system/role/{id}")
    public Result<AdminRoleVO> getRole(@PathVariable("id") Integer id) {
        return Result.success(adminRoleService.getRole(id));
    }

    @Operation(summary = "role ", description = "获取角色权限信息")
    @GetMapping("system/common/dict/data_scope_enum")
    public Result<List<Dict>> getRoleDataScope() {
        List<Dict> list = new ArrayList<>();
        for (IEnum<Integer> integerIEnum : IEnum.getAll(AdminRoleDataScopeEnum.class)) {
            Dict dict = Dict.of("label", integerIEnum.getText(), "value", integerIEnum.getCode());
            list.add(dict);
        }
        return Result.success(list);
    }

    @Operation(summary = "save role", description = "save role")
    @PostMapping("system/role")
    public Result<Integer> saveRole(@Valid @RequestBody RoleSaveDTO dto) {
        return Result.success(adminRoleService.saveRole(dto));
    }

    @Operation(summary = "edit role", description = "edit role")
    @PutMapping("system/role/{id}")
    public Result<Boolean> editRole(@PathVariable("id") Integer id, @Valid @RequestBody RoleSaveDTO dto) {
        return Result.success(adminRoleService.editRole(id, dto));
    }

    @Operation(summary = "delete role", description = "delete role")
    @DeleteMapping("system/role")
    public Result<Boolean> deleteRole(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");
        return Result.success(adminRoleService.deleteRole(ids));
    }

    @Operation(summary = "edit role menu", description = "edit role menu")
    @PutMapping("system/role/{id}/permission")
    public Result<Boolean> editRoleMenu(@PathVariable("id") Integer id, @RequestBody Map<String, List<Integer>> request) {
        List<Integer> menuIds = request.get("menuIds");
        return Result.success(adminRoleService.editRoleMenu(id, menuIds));
    }

    @Operation(summary = "User Tree", description = "获取用户信息")
    @GetMapping("system/role/{id}/user")
    public Result<Dict> getRoleUserList(@RequestParam(name = "page") Integer pageNum,
                                        @RequestParam(name = "size") Integer pageSize,
                                        @PathVariable("id") Integer id,
                                        @RequestParam(name = "status", required = false) Integer status,
                                        @RequestParam(name = "createTime", required = false) String createTime,
                                        @RequestParam(name = "description", required = false) String description) {

        PagedVO<AdminUserVO> pagedVO = adminUserService.getUserList(pageNum, pageSize, null, status, createTime, description, id, null);
        Dict dict = Dict.create().set("list", pagedVO.getList()).set("total", pagedVO.getTotal());
        return Result.success(dict);
    }

    @Operation(summary = "role user", description = "分配角色")
    @PostMapping("system/role/{id}/user")
    public Result<Boolean> saveUserList(@PathVariable("id") Integer id, @RequestBody List<Integer> userId) {
       adminRoleService.saveUserList(id, userId);
       return Result.success(true);
    }

    @Operation(summary = "delete role user rel", description = "delete role rel")
    @DeleteMapping("system/role/{id}/user")
    public Result<Boolean> deleteRoleUserRel(@PathVariable("id") Integer id, @RequestBody List<Integer> userIdList) {
        return Result.success(adminRoleService.deleteRoleUserRel(id, userIdList));
    }
}
