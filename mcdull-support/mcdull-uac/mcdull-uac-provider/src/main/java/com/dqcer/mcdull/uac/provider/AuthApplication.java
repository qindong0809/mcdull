package com.dqcer.mcdull.uac.provider;

import com.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import com.dqcer.mcdull.framework.redis.EnableCache;
import com.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 身份验证应用程序
 *
 * @author QinDong
 * @date 2022/10/31
 */
@EnableWebCore
@MapperScan("com.dqcer.mcdull.auth.provider.web.dao")
@EnableDynamicDataSource
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
