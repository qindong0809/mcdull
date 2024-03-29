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

    Result<RoleVO> detail(RoleLiteDTO dto);

    Integer insert(RoleInsertDTO dto);

    Result<Integer> toggleStatus(RoleLiteDTO dto);

    Result<Integer> delete(UserLiteDTO dto);

    Map<Integer, List<RoleEntity>> getRoleMap(List<Integer> userIdList);

    List<LabelValueVO<Integer, String>> getSimple(Integer userId);

    boolean update(Integer id, RoleUpdateDTO dto);

    boolean delete(Integer id, ReasonDTO dto);

    boolean toggleStatus(Integer id, ReasonDTO dto);

    boolean insertPermission(Integer id, RolePermissionInsertDTO dto);

    Map<Integer, List<RoleEntity>> getRoleMapByMenuId(List<Integer> menuIdList);
}
