package io.gitee.dqcer.mcdull.system.provider.web.controller.administraotr;


import cn.hutool.core.collection.ListUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptTreeVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(GlobalConstant.ADMINISTRATOR_PATH)
@Tag(name = "Administrator")
public class AdminUserController extends BasicController {

    @Resource
    private IAdminDeptService adminDeptService;

    @Operation(summary = "Dept Tree", description = "获取部门信息")
    @GetMapping("system/dept/dict/tree")
    public Result<List<AdminDeptTreeVO>> getDeptTree() {
        return Result.success(ListUtil.of(adminDeptService.getDeptTree()));
    }

    @Operation(summary = "Dept Tree", description = "获取部门信息")
    @GetMapping("system/user")
    public Result<List<AdminDeptTreeVO>> getUserList() {
        return Result.success(ListUtil.of(adminDeptService.getDeptTree()));
    }
}
