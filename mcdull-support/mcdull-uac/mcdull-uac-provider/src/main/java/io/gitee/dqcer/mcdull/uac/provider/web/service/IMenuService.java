package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleMenuTreeVO;

import java.util.List;
import java.util.Map;

/**
 * Menu Service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
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

    /**
     * 获取菜单名称
     *
     * @param codeList 代码列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    Map<String, MenuEntity> getMenuName(List<String> codeList);

    /**
     * 列出全部
     *
     * @return {@link List }<{@link MenuEntity }>
     */
    List<MenuEntity> listAll();
}
