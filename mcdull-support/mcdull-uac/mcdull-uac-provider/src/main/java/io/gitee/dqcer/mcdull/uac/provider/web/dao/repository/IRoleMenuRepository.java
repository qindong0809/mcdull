package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleMenuDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleMenuRepository extends IService<RoleMenuDO> {


    /**
     * 按角色id映射
     *
     * @param roleIdCollection 角色id集合
     * @return {@link Map}<{@link Long}, {@link List}<{@link Long}>>
     */
    Map<Integer, List<Integer>> menuIdListMap(Collection<Integer> roleIdCollection);

    List<RoleMenuDO> listByRoleId(Integer roleId);

    void insert(Integer roleId, List<Integer> menuIdList);
}
