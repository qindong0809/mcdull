package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;

import java.util.List;

/**
 * Role Data Scope repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IRoleDataScopeRepository extends IRepository<RoleDataScopeEntity> {

    /**
     * get list by role
     *
     * @param roleId role id
     * @return {@link List}<{@link RoleDataScopeEntity}>
     */
    List<RoleDataScopeEntity> getListByRole(Integer roleId);

    /**
     * update
     *
     * @param insertList insert list
     * @param updateList update list
     * @param removeList remove list
     */
    void update(List<RoleDataScopeEntity> insertList,
                List<RoleDataScopeEntity> updateList, List<RoleDataScopeEntity> removeList);
}