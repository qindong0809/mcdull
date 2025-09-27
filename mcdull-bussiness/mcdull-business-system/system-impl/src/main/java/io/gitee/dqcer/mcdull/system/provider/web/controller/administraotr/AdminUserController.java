package io.gitee.dqcer.mcdull.system.provider.web.controller.administraotr;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Dict;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.DeptSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.*;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminDeptService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(GlobalConstant.ADMINISTRATOR_PATH)
@Tag(name = "Administrator")
public class AdminUserController extends BasicController {

    @Resource
    private IAdminDeptService adminDeptService;
    @Resource
    private IAdminUserService adminUserService;

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
                                    @RequestParam(name = "deptId") Integer deptId,
                                    @RequestParam(name = "status", required = false) Integer status,
                                    @RequestParam(name = "createTime", required = false) String createTime,
                                    @RequestParam(name = "description", required = false) String description) {
        PagedVO<AdminUserVO> pagedVO = adminUserService.getUserList(pageNum, pageSize, deptId, status, createTime, description);
        Dict dict = Dict.create().set("list", pagedVO.getList()).set("total", pagedVO.getTotal());
        return Result.success(dict);
    }

    @Operation(summary = "get user", description = "get user")
    @GetMapping("system/user/{id}")
    public Result<AdminUserSimpleVO> getUser(@PathVariable("id") Integer id) {
        return Result.success(adminUserService.getUser(id));
    }
}
