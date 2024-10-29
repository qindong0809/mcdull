package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Menu repository
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IMenuRepository extends IRepository<MenuEntity> {

    /**
     * 所有菜单编码
     *
     * @return {@link List}<{@link String}>
     */
    List<String> allCodeList();

    /**
     * 所有菜单
     *
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> allList();

    /**
     * 菜单编码列表映射
     *
     * @param menuListMap 菜单列表映射
     * @return {@link Map}<{@link Integer}, {@link List}<{@link String}>>
     */
    Map<Integer, List<String>> menuCodeListMap(Map<Integer, List<Integer>> menuListMap);

    /**
     * 列表
     *
     * @param collection 集合
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> list(Collection<Integer> collection);

    /**
     * 所有
     *
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> all();

    /**
     * 所有按钮
     *
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> allAndButton();

    /**
     * 根据父id
     *
     * @param parentId 父id
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> listByParentId(Integer parentId);

    /**
     * 删除
     *
     * @param id      id
     * @param reason  原因
     * @return boolean
     */
    boolean delete(Integer id, String reason);

    /**
     * 菜单列表映射
     *
     * @param menuListMap 菜单列表映射
     * @return {@link Map}<{@link Integer}, {@link List}<{@link MenuEntity}>>
     */
    Map<Integer, List<MenuEntity>> getMenuListMap(Map<Integer, List<Integer>> menuListMap);

    /**
     * 菜单列表
     *
     * @param onlyMenu 只菜单
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> listOnlyMenu(Boolean onlyMenu);
}
