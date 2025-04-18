package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;

import java.util.List;
import java.util.Map;

/**
 * Role Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IRoleService {

    RoleVO detail(Integer id);

    void insert(RoleAddDTO dto);

    void delete(Integer id);

    Map<Integer, List<RoleEntity>> getRoleMap(List<Integer> userIdList);

    boolean update(Integer id, RoleUpdateDTO dto);

    boolean delete(Integer id, ReasonDTO dto);

    boolean insertPermission(Integer id, RolePermissionInsertDTO dto);

    List<RoleVO> all();

    RoleVO get(Integer roleId);

    void updateRole(RoleUpdateDTO dto);

    void updateRoleMenu(RoleMenuUpdateDTO dto);

    void batchRemoveRoleEmployee(RoleEmployeeUpdateDTO dto);

    Map<Integer, String> mapName(List<Integer> roleIdList);
}
