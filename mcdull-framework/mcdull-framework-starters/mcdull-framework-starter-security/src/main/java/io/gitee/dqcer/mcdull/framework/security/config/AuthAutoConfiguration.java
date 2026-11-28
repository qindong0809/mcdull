package io.gitee.dqcer.mcdull.framework.security.config;

import cn.dev33.satoken.stp.StpInterface;
import io.gitee.dqcer.mcdull.framework.security.RemoteUserDetailsService;
import io.gitee.dqcer.mcdull.framework.security.advice.SaTokenExceptionAdvice;
import io.gitee.dqcer.mcdull.framework.security.aspect.AuditAspect;
import io.gitee.dqcer.mcdull.framework.security.filter.AuthSessionFilter;
import io.gitee.dqcer.mcdull.framework.spi.permission.UserPermissionResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 鉴权能力自动装配。
 * <p>
 * starter-web 不再传递依赖本模块，因此装配入口是 {@code AutoConfiguration.imports}：
 * 只要把 starter-security 放到 classpath 就完成鉴权接入，无需消费方手工 {@code @Import}。
 *
 * @author dqcer
 * @since 2026/11/19
 */
@AutoConfiguration
@Import(SaTokenExceptionAdvice.class)
public class AuthAutoConfiguration {

    /**
     * 权限码写入会话的切面，{@code @SaCheckEL} 由 sa-token-spring-el 自行织入
     */
    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }

    /**
     * 通用远程权限数据源，业务服务自带本地 StpInterface 时让位。
     * 未配置 UserPermissionResolver 时仍注册一个 NOOP 数据源，避免鉴权时找不到 Bean。
     */
    @Bean
    @ConditionalOnMissingBean(StpInterface.class)
    public StpInterface remoteStpInterface(ObjectProvider<UserPermissionResolver> resolverProvider) {
        UserPermissionResolver resolver = resolverProvider.orderedStream().findFirst()
                .orElse(UserPermissionResolver.NOOP);
        return new RemoteUserDetailsService(resolver);
    }

    /**
     * 会话解析过滤器依赖 servlet，非 web 应用（如网关）不注册
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class AuthSessionFilterConfiguration {

        @Bean
        public FilterRegistrationBean<AuthSessionFilter> authSessionFilterRegistrationBean(
                GatewayProperties gatewayProperties, SaTokenExceptionAdvice saTokenExceptionAdvice) {
            FilterRegistrationBean<AuthSessionFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new AuthSessionFilter(gatewayProperties, saTokenExceptionAdvice));
            // 晚于 starter-web 的 HttpTraceLogFilter(order=2)：需要它先建好 UnifySession 再回填登录态
            registrationBean.setOrder(3);
            registrationBean.setEnabled(true);
            registrationBean.addUrlPatterns("/*");
            return registrationBean;
        }
    }
}
