package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuTreeVo;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuVO;

/**
 * 菜单转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class MenuConvert {

    public static MenuVO convertToMenuVO(MenuEntity menuDO) {
        MenuVO menuVO = new MenuVO();
        menuVO.setId(Math.toIntExact(menuDO.getId()));
        menuVO.setName(menuDO.getName());
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

    public static MenuTreeVo convertMenuTreeVo(MenuEntity menu) {
        MenuTreeVo menuTreeVo = new MenuTreeVo();
        menuTreeVo.setName(menu.getName());
        menuTreeVo.setOrderNum(menu.getOrderNum());
        menuTreeVo.setPath(menu.getPath());
        menuTreeVo.setComponent(menu.getComponent());
        menuTreeVo.setQuery(menu.getQuery());
        menuTreeVo.setIsFrame(menu.getIsFrame());
        menuTreeVo.setIsCache(menu.getIsCache());
        menuTreeVo.setMenuType(menu.getMenuType());
        menuTreeVo.setVisible(menu.getVisible());
        menuTreeVo.setStatus(menu.getStatus());
        menuTreeVo.setPerms(menu.getPerms());
        menuTreeVo.setIcon(menu.getIcon());
        menuTreeVo.setCreatedBy(menu.getCreatedBy());
        menuTreeVo.setUpdatedTime(menu.getUpdatedTime());
        menuTreeVo.setUpdatedBy(menu.getUpdatedBy());
        menuTreeVo.setRemark(menu.getRemark());
        menuTreeVo.setId(menu.getId());
        menuTreeVo.setParentId(menu.getParentId());
        return menuTreeVo;
    }

    public static MenuEntity convertDO(MenuTreeVo treeVo) {
        MenuEntity menuDO = new MenuEntity();
        menuDO.setName(treeVo.getName());
        menuDO.setParentId(treeVo.getParentId());
        menuDO.setOrderNum(treeVo.getOrderNum());
        menuDO.setPath(treeVo.getPath());
        menuDO.setComponent(treeVo.getComponent());
        menuDO.setQuery(treeVo.getQuery());
        menuDO.setIsFrame(treeVo.getIsFrame());
        menuDO.setIsCache(treeVo.getIsCache());
        menuDO.setMenuType(treeVo.getMenuType());
        menuDO.setVisible(treeVo.getVisible());
        menuDO.setStatus(treeVo.getStatus());
        menuDO.setPerms(treeVo.getPerms());
        menuDO.setIcon(treeVo.getIcon());
        menuDO.setCreatedBy(treeVo.getCreatedBy());
        menuDO.setUpdatedTime(treeVo.getUpdatedTime());
        menuDO.setUpdatedBy(treeVo.getUpdatedBy());
        menuDO.setRemark(treeVo.getRemark());
        menuDO.setId(treeVo.getId());
        return menuDO;
    }

    public static MenuEntity convertDoByDto(MenuAddDTO dto) {
        MenuEntity menuDO = new MenuEntity();
        menuDO.setName(dto.getName());
        menuDO.setParentId(dto.getParentId());
        menuDO.setOrderNum(dto.getOrderNum());
        menuDO.setPath(dto.getPath());
        menuDO.setComponent(dto.getComponent());
        menuDO.setQuery(dto.getQuery());
        menuDO.setIsFrame(dto.getIsFrame());
        menuDO.setIsCache(dto.getIsCache());
        menuDO.setMenuType(dto.getMenuType());
        menuDO.setVisible(dto.getVisible());
        menuDO.setStatus(dto.getStatus());
        menuDO.setPerms(dto.getPerms());
        menuDO.setIcon(dto.getIcon());
        menuDO.setRemark(dto.getRemark());
        return menuDO;
    }
}
