package io.gitee.dqcer.mcdull.uac.provider;

import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableMapperScan;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableUacFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
public class UserDataContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDataContentApplication.class, args);
    }
}
