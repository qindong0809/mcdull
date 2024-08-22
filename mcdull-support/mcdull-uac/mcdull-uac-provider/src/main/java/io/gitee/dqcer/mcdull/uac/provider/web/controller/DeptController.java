package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentInfoVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentTreeInfoVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
@Tag(name = "部门员工")
@RequestMapping("dept")
@RestController
public class DeptController {

    @Resource
    private IDepartmentService deptService;

    @Operation(summary = "查询部门全部列表")
    @GetMapping("list-all")
    public Result<List<DepartmentInfoVO>> getAll() {
        return Result.success(deptService.getAll());
    }


    @Operation(summary = "Add")
    @PostMapping("insert")
    @SaCheckPermission("system:department:add")
    public Result<Boolean> insert(@RequestBody @Validated DeptInsertDTO dto){
        return Result.success(deptService.insert(dto));
    }

    @Operation(summary = "Update")
    @PostMapping("update")
    @SaCheckPermission("system:department:update")
    public Result<Boolean> updateDepartment(@Valid @RequestBody DeptUpdateDTO dto) {
        return Result.success(deptService.update(dto.getDepartmentId(), dto));
    }

    @Operation(summary = "Delete ")
    @DeleteMapping("delete/{departmentId}")
    @SaCheckPermission("system:department:delete")
    public Result<Boolean> deleteDepartment(@PathVariable Integer departmentId) {
        return Result.success(deptService.delete(departmentId));
    }

    @Operation(summary = "查询部门树形列表")
    @GetMapping("treeList")
    public Result<List<DepartmentTreeInfoVO>> departmentTree() {
        return Result.success(deptService.departmentTree());
    }


}
