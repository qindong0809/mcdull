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

    public static final String [] EXCLUDE_PATTERNS = {
            GlobalConstant.SSO_LOGIN,
            GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS,
            GlobalConstant.FAVICON_ICO,
            GlobalConstant.ACTUATOR_ALL,
            "/doc.html/**",
            "/swagger-ui.html/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/api-docs/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter
                    .match(GlobalConstant.ALL_PATTERNS)
                    .notMatch(EXCLUDE_PATTERNS)
                    .check(r -> StpUtil.checkLogin());


            // 根据路由划分模块，不同模块不同鉴权
//            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
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
                .addExclude(EXCLUDE_PATTERNS)
                .setAuth(obj -> {
                    // 校验 Same-Token 身份凭证     —— 以下两句代码可简化为：SaSameUtil.checkCurrentRequestToken();
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                .setError(e -> SaResult.error(e.getMessage()))
                ;
    }


}
