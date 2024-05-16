package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface ILoginLogService {

    PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto);

    void add(LoginLogEntity entity);
}
