package com.dqcer.mcdull.admin.config;

import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.framework.web.interceptor.BaseInfoInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleBaseInfoInterceptor extends BaseInfoInterceptor {

    @Override
    protected List<UserPowerVO> getUserPower() {
        return super.getUserPower();
    }

    @Override
    protected boolean enableAuth() {
        return true;
    }
}
