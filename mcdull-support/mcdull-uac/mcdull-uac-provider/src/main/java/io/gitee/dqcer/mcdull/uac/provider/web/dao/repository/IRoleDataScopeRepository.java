package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IRoleDataScopeRepository extends IService<RoleDataScopeEntity>  {

    List<RoleDataScopeEntity> getListByRole(Long roleId);

    void update(List<RoleDataScopeEntity> insertList,
                List<RoleDataScopeEntity> updateList, List<RoleDataScopeEntity> removeList);
}