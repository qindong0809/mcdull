package com.dqcer.mcdull.admin.config;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.web.service.sso.IAuthService;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.framework.web.interceptor.BaseInfoInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SimpleBaseInfoInterceptor extends BaseInfoInterceptor {

    @Resource
    private IAuthService authService;

    @Override
    protected Result<Long> authCheck(String token) {
        return authService.tokenValid(token);
    }

    @Override
    protected List<UserPowerVO> getUserPower() {
        return super.getUserPower();
    }

    @Override
    protected boolean enableAuth() {
        return true;
    }
}
