package com.dqcer.mcdull.admin.web.service.sso;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.model.dto.sys.LoginDTO;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;

import java.util.List;

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
     * 注销
     *
     * @return {@link Result<String>}
     */
    Result<String> logout();
}
