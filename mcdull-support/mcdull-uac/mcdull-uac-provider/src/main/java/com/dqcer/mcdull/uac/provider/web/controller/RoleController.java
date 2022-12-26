package com.dqcer.mcdull.uac.provider.web.controller;

import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.annotation.Transform;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import com.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import com.dqcer.mcdull.uac.provider.model.vo.UserVO;
import com.dqcer.mcdull.uac.provider.web.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 *
 * @author dqcer
 * @version 2022/12/26
 */
@RequestMapping("role")
@RestController
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @GetMapping("base/page")
    @Transform
    public Result<PagedVO<RoleVO>> listByPage(@Validated(ValidGroup.Paged.class) RoleLiteDTO dto) {
        return roleService.listByPage(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    @GetMapping("base/detail")
    @Transform
    public Result<RoleVO> detail(@Validated(ValidGroup.One.class) RoleLiteDTO dto) {
        return roleService.detail(dto);
    }

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回新增主键}
     */
    @PostMapping("base/save")
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Add.class})RoleLiteDTO dto){
        return roleService.insert(dto);
    }

    /**
     * 状态更新
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @PostMapping("base/status")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) RoleLiteDTO dto){
        return roleService.updateStatus(dto);
    }

    /**
     * 单个删除
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @PostMapping("base/delete")
    public Result<Long> delete(@RequestBody @Validated(value = {ValidGroup.Delete.class}) UserLiteDTO dto){
        return roleService.delete(dto);
    }

}
