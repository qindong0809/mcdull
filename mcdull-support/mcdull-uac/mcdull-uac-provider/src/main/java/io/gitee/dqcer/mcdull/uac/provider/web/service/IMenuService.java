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
    Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList);

    Map<Integer, List<MenuEntity>> getMenuListMap(List<Integer> roleIdList);

    List<String> getAllCodeList();

    List<MenuVO> list(MenuListDTO dto);

    void insert(MenuAddDTO dto);

    void update(MenuUpdateDTO dto);

    void delete(List<Integer> menuIdList);

    List<MenuVO> getList(Integer userId, boolean administratorFlag);

    RoleMenuTreeVO getTreeRoleId(Integer roleId);

    List<MenuTreeVO> queryMenuTree(Boolean onlyMenu);
}
