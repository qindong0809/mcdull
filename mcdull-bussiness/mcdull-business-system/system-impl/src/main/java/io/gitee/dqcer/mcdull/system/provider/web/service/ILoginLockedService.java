package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LoginLockedVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  Login Locked Service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
 */

public interface ILoginLockedService {

    LoginLockedEntity get(String loginName);

    PagedVO<LoginLockedVO> queryPage(LoginFailQueryDTO dto);

    void unlock(List<Integer> idList);

    void updateFailCount(String username, Integer failedLoginMaximumTime, int failCount);

    void inactive(String username);

    int getLoginFailureCount(String loginName);
}
