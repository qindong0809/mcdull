package io.gitee.dqcer.mcdull.admin.framework.auth;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserSession;

import java.util.List;

/**
 * security 服务
 *
 * @author dqcer
 * @since 2023/01/15 16:01:75
 */
public interface ISecurityService {

    /**
     * token验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link UserSession}>
     */
    Result<UserSession> tokenValid(String token);


    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link UserPowerVO}>>
     */
    List<UserPowerVO> queryResourceModules(Long userId);
}
