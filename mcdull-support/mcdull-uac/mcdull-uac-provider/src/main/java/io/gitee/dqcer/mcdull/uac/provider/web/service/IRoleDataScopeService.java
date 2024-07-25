package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleDataScopeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DataScopeAndViewTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleDataScopeVO;

import java.util.List;

/**
 * Role Data Scope Service
 *
 * @author dqcer
 * @since 2024/05/13
 */
public interface IRoleDataScopeService {

    List<RoleDataScopeVO> getListByRole(Integer roleId);

    void updateByRoleId(RoleDataScopeUpdateDTO dto);

    List<DataScopeAndViewTypeVO> dataScopeList();
}
