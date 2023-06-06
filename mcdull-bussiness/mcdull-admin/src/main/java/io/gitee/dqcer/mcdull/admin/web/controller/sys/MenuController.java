package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleMenuTreeSelectVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IMenuService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* 菜单 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;


    /**
    * 列表查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Authorized("system:menu:list")
    @GetMapping("list")
    public Result<List<MenuVO>> list(@Validated(value = {ValidGroup.List.class}) MenuLiteDTO dto){
        return menuService.list(dto);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return {@link Result<PagedVO>}
     */
    @Authorized("system:menu:query")
    @GetMapping("/{id}")
    public Result<MenuVO> detail(@PathVariable Long id){
        return menuService.detail(id);
    }

    @GetMapping("roleMenuTreeselect/{roleId}")
    public Result<RoleMenuTreeSelectVO> roleMenuTreeSelect(@PathVariable("roleId") Long roleId){
        return menuService.roleMenuTreeSelect(roleId);
    }

    @GetMapping("/treeselect")
    public Result<RoleMenuTreeSelectVO> treeselect() {
        return menuService.treeselect();
    }

}
