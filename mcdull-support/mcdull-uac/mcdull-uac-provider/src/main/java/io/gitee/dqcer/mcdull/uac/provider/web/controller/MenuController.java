package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
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

    @Resource
    private IRoleService roleService;

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
     * @return {@link Result<Integer> 返回新增主键}
     */
    @PostMapping("insert")
    public Result<Boolean> insert(@RequestBody @Validated MenuInsertDTO dto){
        return Result.success(menuService.insert(dto));
    }

    @PutMapping("{id}/update")
    public Result<Boolean> update(@PathVariable("id") Integer id, @RequestBody @Validated MenuUpdateDTO dto){
        return Result.success(menuService.update(id, dto));
    }

    /**
     * 状态更新
     *
     * @param dto dto
     * @return {@link Result<Integer>}
     */
    @PutMapping("{id}/status")
    public Result<Boolean> toggleStatus(@PathVariable("id") Integer id, @RequestBody ReasonDTO dto){
        return Result.success(roleService.toggleStatus(id, dto));
    }

    /**
     * 单个删除
     *
     * @param dto dto
     * @return {@link Result<Integer>}
     */
    @DeleteMapping("{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id, @Validated ReasonDTO dto){
        return Result.success(menuService.delete(id, dto));
    }

}
