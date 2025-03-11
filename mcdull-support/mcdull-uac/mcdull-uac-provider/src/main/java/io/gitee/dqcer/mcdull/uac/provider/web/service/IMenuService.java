package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
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

    /**
     * 获取菜单代码列表映射
     *
     * @param roleIdList 角色 ID 列表
     * @return {@link Map }<{@link Integer }, {@link List }<{@link String }>>
     */
    Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList);

    /**
     * 获取菜单列表地图
     *
     * @param roleIdList 角色 ID 列表
     * @return {@link Map }<{@link Integer }, {@link List }<{@link MenuEntity }>>
     */
    Map<Integer, List<MenuEntity>> getMenuListMap(List<Integer> roleIdList);

    /**
     * 获取所有代码列表
     *
     * @return {@link List }<{@link String }>
     */
    List<String> getAllCodeList();

    /**
     * 列表
     *
     * @param dto DTO
     * @return {@link List }<{@link MenuVO }>
     */
    List<MenuVO> list(MenuListDTO dto);

    /**
     * 插入
     *
     * @param dto DTO
     */
    void insert(MenuAddDTO dto);

    /**
     * 更新
     *
     * @param dto DTO
     */
    void update(MenuUpdateDTO dto);

    /**
     * 删除
     *
     * @param menuIdList 菜单 ID 列表
     */
    void delete(List<Integer> menuIdList);

    /**
     * 获取列表
     *
     * @param userId            用户 ID
     * @param administratorFlag 管理员标志
     * @return {@link List }<{@link MenuVO }>
     */
    List<MenuVO> getList(Integer userId, boolean administratorFlag);

    /**
     * 获取树角色 ID
     *
     * @param roleId 角色 ID
     * @return {@link RoleMenuTreeVO }
     */
    RoleMenuTreeVO getTreeRoleId(Integer roleId);

    /**
     * Query 菜单树
     *
     * @param onlyMenu Only 菜单
     * @return {@link List }<{@link MenuTreeVO }>
     */
    List<MenuTreeVO> queryMenuTree(Boolean onlyMenu);

    /**
     * 导出数据
     *
     * @param dto DTO
     */
    boolean exportData(MenuListDTO dto);

    /**
     * 获取下拉选项
     *
     * @return {@link List }<{@link LabelValueVO }<{@link String }, {@link String }>>
     */
    List<LabelValueVO<String, String>> getDropdownOptions();

    /**
     * 获取当前菜单名称
     *
     * @return {@link String }
     */
    List<String> getCurrentMenuName();
}
