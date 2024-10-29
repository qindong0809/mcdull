package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleMenuEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Role Menu Repository
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleMenuRepository extends IRepository<RoleMenuEntity> {

    /**
     * 按角色id映射
     *
     * @param roleIdCollection 角色id集合
     * @return {@link Map}<{@link Long}, {@link List}<{@link Long}>>
     */
    Map<Integer, List<Integer>> menuIdListMap(Collection<Integer> roleIdCollection);

    /**
     * 按角色id查询
     *
     * @param roleId 角色id
     * @return {@link List}<{@link RoleMenuEntity}>
     */
    List<RoleMenuEntity> listByRoleId(Integer roleId);

    /**
     * 删除
     *
     * @param roleId 角色id
     */
    void insert(Integer roleId, List<Integer> menuIdList);

    /**
     * 按菜单id查询
     *
     * @param menuIdList 菜单id列表
     * @return {@link Map}<{@link Integer}, {@link List}<{@link Integer}>>
     */
    Map<Integer, List<Integer>> listByMenuIdList(List<Integer> menuIdList);
}
