package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IRoleService {
    PagedVO<RoleVO> listByPage(RolePageDTO dto);

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
}
