package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLockedVO;

import java.util.List;

/**
 *  Login Locked Service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
 */

public interface ILoginLockedService {

    LoginLockedEntity get(String loginName);

    void lock(String loginName, Integer failedLoginMaximumTime);

    void updateFailCount(LoginLockedEntity lockedEntity);

    PagedVO<LoginLockedVO> queryPage(LoginFailQueryDTO dto);

    void unlock(List<Integer> idList);
}
