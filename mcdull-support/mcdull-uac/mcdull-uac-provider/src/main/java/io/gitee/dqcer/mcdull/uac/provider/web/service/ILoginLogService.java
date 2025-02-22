package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;

/**
 *  Login Log Service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
 */

public interface ILoginLogService {

    PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto);

    void add(LoginLogEntity entity);

    LoginLogEntity getFirstLoginLog(String loginName);

    LoginLogEntity getLastLoginLog(String loginName);

    void exportData(LoginLogQueryDTO dto);
}
