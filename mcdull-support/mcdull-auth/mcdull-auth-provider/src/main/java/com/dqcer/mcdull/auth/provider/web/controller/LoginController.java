package com.dqcer.mcdull.auth.provider.web.controller;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.auth.api.dto.LoginDTO;
import com.dqcer.mcdull.auth.api.vo.LoginVO;
import com.dqcer.mcdull.auth.provider.web.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("")
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link LoginVO}>
     */
    @PostMapping("/login")
    public Result<String> login(@Valid LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }
}
