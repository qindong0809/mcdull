package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleMenuTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleMenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RequestMapping("menu")
@RestController
public class MenuController {

    @Resource
    private IMenuService menuService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @GetMapping("list")
    public Result<List<MenuVO>> list(@Validated MenuListDTO dto) {
        return Result.success(menuService.list(dto));
    }

    @Operation(summary = "获取角色关联菜单权限")
    @GetMapping("{roleId}/tree")
    public Result<RoleMenuTreeVO> getTreeRoleId(@PathVariable Long roleId) {
        return Result.success(menuService.getTreeRoleId(roleId));
    }


    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Integer> 返回新增主键}
     */
    @PostMapping("insert")
    public Result<Boolean> insert(@RequestBody @Validated MenuInsertDTO dto){
        return Result.success(menuService.insert(dto));
    }

    @PutMapping("{id}/update")
    public Result<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated MenuUpdateDTO dto){
        return Result.success(menuService.update(id, dto));
    }

    /**
     * 单个删除
     *
     * @param dto dto
     * @return {@link Result<Integer>}
     */
    @DeleteMapping("{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id, @Validated ReasonDTO dto){
        return Result.success(menuService.delete(id, dto));
    }

    @GetMapping("role-menu")
    public Result<List<RoleMenuVO>> roleMenuList(){
        return Result.success(menuService.roleMenuList());
    }

    @GetMapping("{roleId}/role-menu-ids")
    public Result<List<Long>> roleMenuIdList(@PathVariable("roleId") Long roleId){
        return Result.success(menuService.roleMenuIdList(roleId));
    }

}
