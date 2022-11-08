package com.dqcer.mcdull.auth.provider.web.controller;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.auth.api.dto.LoginDTO;
import com.dqcer.mcdull.auth.api.vo.LoginVO;
import com.dqcer.mcdull.auth.client.api.AuthServiceApi;
import com.dqcer.mcdull.auth.provider.web.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("")
@RestController
public class LoginController implements AuthServiceApi {

    @Resource
    private LoginService loginService;

    /**
     * 登录 21232F297A57A5A743894A0E4A801FC3
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link LoginVO}>
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    /**
     * 验证token
     *
     * @param token token
     * @return {@link Long}
     */
    @Override
    public Result<Long> tokenValid(String token) {
        return loginService.tokenValid(token);
    }
}
