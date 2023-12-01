package io.gitee.dqcer.mcdull.framework.security;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 *
 * @author dqcer
 * @since 2023/12/01
 */
public abstract class AbstractWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            SaRouter.match(GlobalConstant.ALL_PATTERNS, GlobalConstant.SSO_LOGIN, r -> StpUtil.checkLogin());
            // 角色校验 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
//            SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
            SaRouter.notMatch(GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS);
        })).addPathPatterns(GlobalConstant.ALL_PATTERNS);
    }

    /**
     * 注册 Sa-Token 全局过滤器
     *
     * @return {@link SaServletFilter}
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude(GlobalConstant.ALL_PATTERNS)
                .addExclude(GlobalConstant.FAVICON_ICO)
                .setAuth(obj -> {
                    // 校验 Same-Token 身份凭证     —— 以下两句代码可简化为：SaSameUtil.checkCurrentRequestToken();
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                .setError(e -> SaResult.error(e.getMessage()))
                ;
    }


}
