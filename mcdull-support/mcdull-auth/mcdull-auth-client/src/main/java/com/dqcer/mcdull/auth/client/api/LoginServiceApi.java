package com.dqcer.mcdull.auth.client.api;

import com.dqcer.mcdull.auth.api.dto.LoginDTO;
import com.dqcer.mcdull.auth.api.vo.UserLoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 登录服务api
 *
 * @author dongqin
 * @date 2022/10/28
 */
public interface LoginServiceApi {

    /**
     * 登录（账号&密码）
     *
     * @param dto dto
     * @return {@link UserLoginVO}
     */
    @PostMapping("user/login")
    UserLoginVO login(@RequestBody LoginDTO dto);

}
