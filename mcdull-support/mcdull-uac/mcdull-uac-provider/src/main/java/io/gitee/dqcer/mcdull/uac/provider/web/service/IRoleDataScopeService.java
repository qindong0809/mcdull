package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleDataScopeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleDataScopeVO;

import java.util.List;

/**
 * 数据范围服务
 *
 * @author dqcer
 * @since 2024/05/13
 */
public interface IRoleDataScopeService {

    List<RoleDataScopeVO> getListByRole(Long roleId);

    void updateByRoleId(RoleDataScopeUpdateDTO dto);
}
