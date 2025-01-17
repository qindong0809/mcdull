package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleUpdateDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IRoleService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
* 角色表 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/role")
public class RoleController implements BasicController {

    @Resource
    private IRoleService roleService;

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Result}
     */
    @Authorized("system:role:query")
    @GetMapping("list")
    public Result<PagedVO<RoleVO>> listByPage(@Validated(value = {ValidGroup.List.class}) RoleLiteDTO dto){
        return roleService.listByPage(dto);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param roleId roleId
     * @return {@link Result<RoleVO>}
     */
    @GetMapping("{roleId}")
    public Result<RoleVO> detail(@PathVariable Long roleId){
        return roleService.detail(roleId);
    }

    /**
    * 新增数据
    *
    * @param dto dto
    * @return {@link Result<Long> 返回新增主键}
    */
    @Authorized("system:role:add")
    @PostMapping("")
    public Result<Long> insert(@RequestBody RoleInsertDTO dto){
        return roleService.insert(dto);
    }


    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("system:role:edit")
    @PutMapping("")
    public Result<Long> update(@RequestBody RoleUpdateDTO dto){
        return roleService.update(dto);
    }
    /**
    * 状态更新
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @PutMapping("changeStatus")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return roleService.updateStatus(dto);
    }

    /**
    * 根据主键删除
    *
    * @param roleId roleId
    * @return {@link Result<Long>}
    */
    @Authorized("system:role:remove")
    @DeleteMapping("{roleId}")
    public Result<Long> deleteBatchById(@PathVariable Long roleId){
        return roleService.deleteById(roleId);
    }
}
