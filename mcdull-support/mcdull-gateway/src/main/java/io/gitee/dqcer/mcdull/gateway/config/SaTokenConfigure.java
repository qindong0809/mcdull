package io.gitee.dqcer.mcdull.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import io.gitee.dqcer.mcdull.gateway.properties.GatewayProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SaTokenConfigure {
    private static final Logger log = LogManager.getLogger(SaTokenConfigure.class);

    /** 与 StpKit.ADMIN 保持同一 loginType，避免 ADMIN token 被 DEFAULT 会话误判为未登录。 */
    private static final String ADMIN_LOGIN_TYPE = "admin";
    private static final StpLogic ADMIN_STP_LOGIC = new StpLogic(ADMIN_LOGIN_TYPE);
    private static final String[] ADMINISTRATOR_PATH = {
            "/api/system/administrator/**",
    };

    private final GatewayProperties gatewayProperties;

    public SaTokenConfigure(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        SaReactorFilter filter = new SaReactorFilter()
            // 拦截地址
            .addInclude("/**");

        // 免鉴权地址来自 mcdull.gateway.no-auth，与业务服务共用同一套配置约定；
        // 登录相关接口必须放行，否则未登录时请求验证码会被判定未登录，前端 401 后 reload 形成死循环
        List<String> noAuth = gatewayProperties.getNoAuth();
        if (noAuth.isEmpty()) {
            log.warn("mcdull.gateway.no-auth 为空，登录接口也会被鉴权拦截");
        }
        noAuth.forEach(filter::addExclude);
        log.info("Sa-Token 免鉴权路径: {}", noAuth);

        // 直接从redis 查询判断是否已登录
        return filter
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {
                // 管理端使用独立会话体系，避免 ADMIN token 被 DEFAULT 会话判定为未登录
                SaRouter.match(ADMINISTRATOR_PATH)
                        .check(r -> ADMIN_STP_LOGIC.checkLogin());
                // 其他路由仍使用默认会话；显式排除管理端，避免同一请求被两套会话重复校验
                SaRouter.match("/**")
                        .notMatch(ADMINISTRATOR_PATH)
                        .check(r -> StpUtil.checkLogin());
            })
            // 异常处理方法：每次setAuth函数出现异常时进入
            .setError(e -> {
                log.error("SaToken error", e);
                return SaResult.error(e.getMessage());
            })
            ;
    }
}
