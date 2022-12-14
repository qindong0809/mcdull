package com.dqcer.mcdull.uac.provider;

import com.dqcer.mcdull.framework.redis.EnableCache;
import com.dqcer.mcdull.framework.web.EnableWebCore;
import com.dqcer.mcdull.uac.provider.config.EnableMapperScan;
import com.dqcer.mcdull.uac.provider.config.EnableUacFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 身份验证应用程序
 *
 * @author dqcer
 * @version 2022/10/31
 */
@EnableTransactionManagement
@EnableWebCore
@EnableMapperScan
@EnableUacFeignClients
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
public class UserDataContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDataContentApplication.class, args);
    }
}
