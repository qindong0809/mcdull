package io.gitee.dqcer.mcdull.admin.framework.auth;

import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.interceptor.BaseInfoInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 简单的基础信息拦截器
 *
 * @author dqcer
 * @date 2023/01/15 15:01:50
 */
public class SecurityInterceptor extends BaseInfoInterceptor {

    @Resource
    private ISecurityService securityService;

    /**
     * token 验证
     *
     * @param token 令牌
     * @return {@link Result<Long>}
     */
    @Override
    protected Result<Long> authCheck(String token) {
        return securityService.tokenValid(token);
    }

    /**
     * 获取当前用户模块权限
     *
     * @return {@link List<UserPowerVO>}
     */
    @Override
    protected List<UserPowerVO> getUserPower() {
        return securityService.queryResourceModules(UserContextHolder.getSession().getUserId());
    }

    /**
     * 是否启用token验证
     *
     * @return boolean
     */
    @Override
    protected boolean enableAuth() {
        return true;
    }
}
