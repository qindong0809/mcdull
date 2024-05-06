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

    RoleVO detail(Long id);

    void insert(RoleAddDTO dto);

    void delete(Long id);

    Map<Long, List<RoleEntity>> getRoleMap(List<Long> userIdList);

    boolean update(Long id, RoleUpdateDTO dto);

    boolean delete(Long id, ReasonDTO dto);

    boolean toggleStatus(Long id, ReasonDTO dto);

    boolean insertPermission(Long id, RolePermissionInsertDTO dto);

    Map<Long, List<RoleEntity>> getRoleMapByMenuId(List<Long> menuIdList);

    List<RoleVO> all();

    RoleVO get(Long roleId);

    void updateRole(RoleUpdateDTO dto);

    void updateRoleMenu(RoleMenuUpdateDTO dto);
}
