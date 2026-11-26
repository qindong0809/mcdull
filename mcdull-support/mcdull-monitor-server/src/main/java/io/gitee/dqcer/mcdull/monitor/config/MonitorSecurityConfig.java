package io.gitee.dqcer.mcdull.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 监控平台安全配置。
 *
 * <p>监控平面为独立信任域，凭据由环境变量注入且缺失即启动失败，
 * 与项目其他组件的 fail-closed 风格保持一致。
 */
@Configuration
@EnableWebSecurity
public class MonitorSecurityConfig {

    @Value("${mcdull.monitor.user:admin}")
    private String user;

    @Value("${mcdull.monitor.password}")
    private String password;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("mcdull.monitor.password must be set (MONITOR_SECURITY_PASSWORD)");
        }
        if (password.length() < 12) {
            throw new IllegalStateException("mcdull.monitor.password must be at least 12 characters");
        }
        // monitor 为独立支撑模块，不引入 framework-api-security；凭据只从环境变量注入，
        // 且仅存在于内存，使用 {noop} 避免额外依赖。生产应配合内网绑定与 HTTPS。
        UserDetails details = User.withUsername(user)
                .password("{noop}" + password)
                .roles("MONITOR")
                .build();
        return new InMemoryUserDetailsManager(details);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/favicon.ico", "/assets/**", "/error", "/actuator/health").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form.defaultSuccessUrl("/", true))
            .logout(Customizer.withDefaults())
            // SBA 实例详情通过 SSE 推送，禁用 CSRF 仅对 Actuator 风格的实例操作生效；
            // Spring Security 6 默认不对 SSE GET 请求做 CSRF 校验，此处无需额外放行
            .csrf(Customizer.withDefaults());
        return http.build();
    }
}
