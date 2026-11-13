package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * Role Menu Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IRoleMenuService extends IRepository<RoleMenuEntity> {

    /**
     * get mao
     *
     * @param roleIdList 角色ID列表
     * @return {@link Map }<{@link Integer }, {@link List }<{@link Integer }>>
     */
    Map<Integer, List<Integer>> getMenuIdListMap(List<Integer> roleIdList);

    /**
     * delete
     *
     * @param id         id
     * @param menuIdList menuIdList
     * @return boolean
     */
    boolean deleteAndInsert(Integer id, List<Integer> menuIdList);
}
