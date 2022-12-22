package com.dqcer.mcdull.uac.provider;

import com.dqcer.mcdull.framework.redis.EnableCache;
import com.dqcer.mcdull.framework.web.EnableWebCore;
import com.dqcer.mcdull.uac.provider.config.EnableMapperScan;
import com.dqcer.mcdull.uac.provider.config.EnableUacFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 身份验证应用程序
 *
 * @author dqcer
 * @version 2022/10/31
 */
@EnableWebCore
@EnableMapperScan
@EnableUacFeignClients
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.dqcer.mcdull.framework.web.feign.service", "com.dqcer.mcdull.uac.client", "com.dqcer.mcdull.mdc.client"})
public class UserDataContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDataContentApplication.class, args);
    }
}
