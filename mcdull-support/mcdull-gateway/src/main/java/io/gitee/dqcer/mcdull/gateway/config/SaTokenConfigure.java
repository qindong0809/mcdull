package io.gitee.dqcer.mcdull.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaTokenConfigure {
    private static final Logger log = LogManager.getLogger(SaTokenConfigure.class);

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
            // 拦截地址
            .addInclude("/**")    /* 拦截全部path */
            // 开放地址：登录相关接口必须放行，否则未登录时请求验证码会被判定未登录，
            // 前端收到 401 后 reload，重载后再次请求验证码，形成无限循环
            .addExclude("/favicon.ico")
            // 登录页免鉴权接口：验证码、是否开启验证码、登录本身
            .addExclude("/api/system/login/getCaptcha")
            .addExclude("/api/system/login/enabled-captcha")
            .addExclude("/api/system/login")
            .addExclude("/user/doLogin")
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {
                System.out.println(obj);
                // 登录校验 -- 拦截所有路由，开放地址见上方 addExclude
                SaRouter.match("/**", r -> StpUtil.checkLogin());
            })
            // 异常处理方法：每次setAuth函数出现异常时进入
            .setError(e -> {
                log.error("SaToken error", e);
                return SaResult.error(e.getMessage());
            })
            ;
    }
}
