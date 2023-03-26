package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuVO;

/**
 * 菜单转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class MenuConvert {

    public static MenuVO convertToMenuVO(MenuDO menuDO) {
        MenuVO menuVO = new MenuVO();
        menuVO.setMenuId(Math.toIntExact(menuDO.getId()));
        menuVO.setMenuName(menuDO.getName());
        menuVO.setParentId(Math.toIntExact(menuDO.getParentId()));
        menuVO.setOrderNum(menuDO.getOrderNum());
        menuVO.setPath(menuDO.getPath());
        menuVO.setComponent(menuDO.getComponent());
        menuVO.setQuery(menuDO.getQuery());
        menuVO.setIsFrame(menuDO.getIsFrame());
        menuVO.setIsCache(menuDO.getIsCache());
        menuVO.setMenuType(menuDO.getMenuType());
        menuVO.setVisible(menuDO.getVisible());
        menuVO.setStatus(menuDO.getStatus());
        menuVO.setPerms(menuDO.getPerms());
        menuVO.setIcon(menuDO.getIcon());
        menuVO.setCreatedBy(menuDO.getCreatedBy());
        menuVO.setCreatedTime(menuDO.getCreatedTime());
        menuVO.setUpdatedTime(menuDO.getUpdatedTime());
        menuVO.setUpdatedBy(menuDO.getUpdatedBy());
        menuVO.setRemark(menuDO.getRemark());
        return menuVO;

    }


}
