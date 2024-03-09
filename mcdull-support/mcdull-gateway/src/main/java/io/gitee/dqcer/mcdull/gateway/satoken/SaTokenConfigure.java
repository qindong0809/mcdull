package io.gitee.dqcer.mcdull.gateway.satoken;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author dqcer
 */
//@Configuration
public class SaTokenConfigure {

    private static final Logger log = LoggerFactory.getLogger(SaTokenConfigure.class);

    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude(GlobalConstant.ALL_PATTERNS)
                // 开放地址
                .addExclude(GlobalConstant.FAVICON_ICO, GlobalConstant.ACTUATOR_ALL)
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match(GlobalConstant.ALL_PATTERNS)
                            .notMatch("/uac" + GlobalConstant.LOGIN_URL, "/uac" + GlobalConstant.LOGIN_CAPTCHA_IMAGE)
                            .check(r -> StpUtil.checkLogin());
                    // 权限认证 -- 不同模块, 校验不同权限
//                    SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    log.error("setAuth函数出现异常", e);
                    return SaResult.error(e.getMessage());
                })
                ;
    }
}
