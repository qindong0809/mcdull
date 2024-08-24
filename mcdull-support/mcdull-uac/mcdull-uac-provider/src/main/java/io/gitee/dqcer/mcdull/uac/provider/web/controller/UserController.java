package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserAllVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Tag(name = "部门员工")
@RestController
public class UserController extends BasicController {

    @Resource
    private IUserService userService;


    @Authorized("sys:user:view")
    @Operation(summary = "Query Page", description = "")
    @GetMapping("user/list")
    @RedisLock(key = "'lock:uac:user:' + #dto.pageSize ", timeout = 3)
//    @Transform
    public Result<PagedVO<UserVO>> listByPage(@Validated(ValidGroup.Paged.class) UserListDTO dto) {
//        ThreadUtil.sleep(8000);
        return Result.success(userService.listByPage(dto));
    }

    @Operation(summary = "Update Current User Password")
    @PostMapping("/update/password")
    public Result<Boolean> updatePassword(@Valid @RequestBody UserUpdatePasswordDTO dto) {
        userService.updatePassword(UserContextHolder.userId(), dto);
        return Result.success(true);
    }

    @Operation(summary = "添加员工(返回添加员工的密码)")
    @PostMapping("/user/add")
    @SaCheckPermission("system:employee:add")
    public Result<Integer> insert(@Valid @RequestBody UserAddDTO dto) throws Exception {
        return Result.success(super.locker(dto.getLoginName(), 0, () -> userService.insert(dto)));
    }

    @Operation(summary = "查询某个角色下的员工列表")
    @PostMapping("/role/employee/queryEmployee")
    public Result<PagedVO<UserVO>> query(@Valid @RequestBody RoleUserQueryDTO dto) {
        return Result.success(userService.query(dto));
    }

    @Operation(summary = "获取某个角色下的所有员工列表(无分页)")
    @GetMapping("/role/employee/getAllEmployeeByRoleId/{roleId}")
    public Result<List<UserVO>> getAllRoleId(@PathVariable Integer roleId) {
        return Result.success(userService.getAllByRoleId(roleId));
    }

    @Operation(summary = "角色成员列表中批量添加员工")
    @PostMapping("/role/employee/batchAddRoleEmployee")
    @SaCheckPermission("system:role:employee:add")
    public Result<Boolean> addUserListByRole(@Valid @RequestBody RoleUserUpdateDTO dto) {
        userService.addUserListByRole(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新员工禁用/启用状态")
    @GetMapping("/employee/update/disabled/{userId}")
    @SaCheckPermission("system:employee:disabled")
    public Result<Boolean> updateDisableFlag(@PathVariable Integer userId) {
        userService.toggleActive(userId);
        return Result.success(true);
    }

    @Operation(summary = "批量删除员工")
    @PostMapping("/employee/update/batch/delete")
    @SaCheckPermission("system:employee:delete")
    public Result<Boolean> batchUpdateDeleteFlag(@RequestBody List<Integer> userIdList) {
        userService.delete(userIdList);
        return Result.success(true);
    }

    @Operation(summary = "更新数据", description = "")
    @PostMapping("/user/update")
    @SaCheckPermission("system:employee:update")
    public Result<Integer> update(@RequestBody @Validated UserUpdateDTO dto){
        return Result.success(userService.update(dto.getEmployeeId(), dto));
    }

    @Operation(summary = "修改密码", description = "")
    @PostMapping("user/{id}/update-password")
    public Result<Integer> updatePassword(@PathVariable("id") Integer id,
                                       @RequestBody UserUpdatePasswordDTO dto){
        return Result.success(userService.updatePassword(id, dto));
    }

    @Operation(summary = "Reset Password")
    @PostMapping("/user/update/password/reset/{userId}")
    @SaCheckPermission("system:employee:password:reset")
    public Result<String> resetPassword(@PathVariable Integer userId) {
        return Result.success(userService.resetPassword(userId));
    }

    @Operation(summary = "批量调整员工部门")
    @PostMapping("/user/update/batch/department")
    @SaCheckPermission("system:employee:department:update")
    public Result<Boolean> batchUpdateDepartment(@Valid @RequestBody UserBatchUpdateDepartmentDTO dto) {
        userService.batchUpdateDepartment(dto);
        return Result.success(true);
    }


    @Operation(summary = "查询所有员工")
    @GetMapping("/user/queryAll")
    public Result<List<UserAllVO>> queryAll(@RequestParam(value = "disabledFlag", required = false) Boolean disabledFlag) {
        return Result.success(userService.queryAll(disabledFlag));
    }
}
