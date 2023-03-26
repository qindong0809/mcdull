package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IRoleService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.dto.IdDTO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
* 角色表 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Result< PagedVO >}
     */
    @Authorized("system:role:list")
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
    @Authorized("sys:role:insert")
    @PostMapping("base/insert")
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class}) RoleLiteDTO dto){
        return roleService.insert(dto);
    }



    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:role:update")
    @PutMapping("base/update")
    public Result<Long> update(@RequestBody @Validated(value = {ValidGroup.Update.class}) RoleLiteDTO dto){
        return roleService.update(dto);
    }
    /**
    * 状态更新
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:role:status")
    @PutMapping("base/status")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return roleService.updateStatus(dto);
    }

    /**
    * 根据主键删除
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:role:delete")
    @PostMapping("base/delete")
    public Result<List<Long>> deleteBatchByIds(@RequestBody @Valid IdDTO<Long> dto){
        return roleService.deleteBatchByIds(dto);
    }
}
