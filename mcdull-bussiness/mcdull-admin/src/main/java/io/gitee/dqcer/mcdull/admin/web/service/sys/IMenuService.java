package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleMenuTreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 菜单服务 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IMenuService {

    Result<List<MenuVO>> list(MenuLiteDTO dto);

    Result<MenuVO> detail(Long id);

    Result<RoleMenuTreeSelectVO> roleMenuTreeSelect(Long roleId);

    Result<RoleMenuTreeSelectVO> treeselect();

}
