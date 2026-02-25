package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleMenuTreeVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Menu Service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
 */

public interface IMenuService extends IRepository<MenuEntity> {

    /**
     * get menu code list map
     *
     * @param roleIdList 角色 ID 列表
     * @return {@link Map }<{@link Integer }, {@link List }<{@link String }>>
     */
    Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList);

    /**
     * get menu list map
     *
     * @param roleIdList 角色 ID 列表
     * @return {@link Map }<{@link Integer }, {@link List }<{@link MenuEntity }>>
     */
    Map<Integer, List<MenuEntity>> getMenuListMap(List<Integer> roleIdList);

    /**
     * get all code list
     *
     * @return {@link List }<{@link String }>
     */
    List<String> getAllCodeList();

    /**
     * list
     *
     * @param dto DTO
     * @return {@link List }<{@link MenuVO }>
     */
    List<MenuVO> list(MenuListDTO dto);

    /**
     * insert
     *
     * @param dto DTO
     */
    void insert(MenuAddDTO dto);

    /**
     * update
     *
     * @param dto DTO
     */
    void update(MenuUpdateDTO dto);

    /**
     * 删除
     *
     * @param menuIdList menuIdList
     */
    void delete(List<Integer> menuIdList);

    /**
     * get list
     *
     * @param userId            userId
     * @param administratorFlag administratorFlag
     * @return {@link List }<{@link MenuVO }>
     */
    List<MenuVO> getList(Integer userId, boolean administratorFlag);

    /**
     * get tree role id
     *
     * @param roleId roleId
     * @return {@link RoleMenuTreeVO }
     */
    RoleMenuTreeVO getTreeRoleId(Integer roleId);

    /**
     * Query menu tree
     *
     * @param onlyMenu onlyMenu
     * @return {@link List }<{@link MenuTreeVO }>
     */
    List<MenuTreeVO> queryMenuTree(Boolean onlyMenu);

    /**
     * export data
     *
     * @param dto DTO
     */
    boolean exportData(MenuListDTO dto);

    /**
     * get dropdown options
     *
     * @return {@link List }<{@link LabelValueVO }<{@link String }, {@link String }>>
     */
    List<LabelValueVO<String, String>> getDropdownOptions();

    /**
     * get current menu name
     *
     * @return {@link String }
     */
    List<String> getCurrentMenuName();

    /**
     * import menu
     *
     * @param file file
     * @return <{@link Boolean }>
     */
    Boolean importMenu(MultipartFile file) throws IOException;
}
