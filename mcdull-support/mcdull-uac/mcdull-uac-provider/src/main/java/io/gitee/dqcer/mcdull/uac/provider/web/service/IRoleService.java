package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IRoleService {
    PagedVO<RoleVO> listByPage(RoleLiteDTO dto);

    Result<RoleVO> detail(RoleLiteDTO dto);

    Result<Long> insert(RoleLiteDTO dto);

    Result<Long> updateStatus(RoleLiteDTO dto);

    Result<Long> delete(UserLiteDTO dto);

    Map<Long, List<RoleDO>> getRoleMap(List<Long> userIdList);

}
