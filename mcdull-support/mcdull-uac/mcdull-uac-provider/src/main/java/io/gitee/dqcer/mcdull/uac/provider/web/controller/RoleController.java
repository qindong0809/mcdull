package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RolePageDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RolePermissionInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RequestMapping("role")
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
    @GetMapping("list")
    public Result<PagedVO<RoleVO>> listByPage(@Validated RolePageDTO dto) {
        return Result.success(roleService.listByPage(dto));
    }

    @GetMapping("list-all")
    public Result<List<RoleVO>> getListAll() {
        return Result.success(roleService.all());
    }


    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Integer> 返回新增主键}
     */
    @PostMapping("insert")
    public Result<Long> insert(@RequestBody @Validated RoleInsertDTO dto){
        return Result.success(roleService.insert(dto));
    }

    @PutMapping("{id}/update")
    public Result<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated RoleUpdateDTO dto){
        return Result.success(roleService.update(id, dto));
    }

    /**
     * 状态更新
     *
     * @param dto dto
     * @return {@link Result<Integer>}
     */
    @PutMapping("{id}/status")
    public Result<Boolean> toggleStatus(@PathVariable("id") Long id, @RequestBody ReasonDTO dto){
        return Result.success(roleService.toggleStatus(id, dto));
    }

    /**
     * 单个删除
     *
     * @param dto dto
     * @return {@link Result<Integer>}
     */
    @DeleteMapping("{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id, @Validated ReasonDTO dto){
        return Result.success(roleService.delete(id, dto));
    }

    @PutMapping("{id}/permission")
    public Result<Boolean> insertPermission(@PathVariable("id") Long id, @RequestBody RolePermissionInsertDTO dto){
        return Result.success(roleService.insertPermission(id, dto));
    }

}
