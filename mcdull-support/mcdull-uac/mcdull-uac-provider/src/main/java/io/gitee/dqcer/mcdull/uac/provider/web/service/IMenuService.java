package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleMenuTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleMenuVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IMenuService {
    Map<Long, List<String>> getMenuCodeListMap(List<Long> roleIdList);

    Map<Long, List<MenuEntity>> getMenuListMap(List<Long> roleIdList);

    List<String> getAllCodeList();

    List<MenuVO> list(MenuListDTO dto);

    void insert(MenuAddDTO dto);

    void update(MenuUpdateDTO dto);

    void delete(List<Long> menuIdList);

    List<RoleMenuVO> roleMenuList();

    List<Long> roleMenuIdList(Long roleId);

    List<MenuVO> getList(Long userId, boolean administratorFlag);

    RoleMenuTreeVO getTreeRoleId(Long roleId);

    List<MenuTreeVO> queryMenuTree(Boolean onlyMenu);
}
