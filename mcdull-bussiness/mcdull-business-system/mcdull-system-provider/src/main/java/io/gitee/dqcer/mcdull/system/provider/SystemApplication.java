package io.gitee.dqcer.mcdull.system.provider;

import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.system.provider.config.EnableMapperScan;
import io.gitee.dqcer.mcdull.system.provider.config.Ip2RegionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * User data content application
 *
 * @author dqcer
 * @since 2022/10/31
 */
@EnableWebCore
@EnableMapperScan
@EnableCache
@SpringBootApplication
@RestController
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SystemApplication.class);
        application.addListeners(new Ip2RegionListener());
        application.run(args);
    }

    /**
     * 单体项目配置
     */
    @Bean
    public CorsFilter corsFilter () {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
