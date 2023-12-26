package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.RoleService;
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
    private RoleService roleService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @GetMapping("base/list")
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
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class})RoleLiteDTO dto){
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
