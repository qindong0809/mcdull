package com.dqcer.mcdull.admin.web.service.sso;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.model.dto.sys.LoginDTO;

/**
 * 身份验证服务 接口定义
 *
 * @author dqcer
 * @date 2023/01/11 22:01:36
 */
public interface IAuthService {

    /**
     * 登录
     *
     * @param loginDTO 登录dto
     * @return {@link Result<String>}
     */
    Result<String> login(LoginDTO loginDTO);

    /**
     * token验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link Long}>
     */
    Result<Long> tokenValid(String token);

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    Result<String> logout();
}
