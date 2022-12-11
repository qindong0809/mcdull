package com.dqcer.mcdull.uac.provider;

import com.dqcer.mcdull.framework.redis.EnableCache;
import com.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan("com.dqcer.mcdull.uac.provider.web.dao.mapper")
@EnableFeignClients(basePackages = {"com.dqcer.mcdull.*.client.service", "com.dqcer.mcdull.framework.web.remote"})
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
public class UserDataContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDataContentApplication.class, args);
    }
}
