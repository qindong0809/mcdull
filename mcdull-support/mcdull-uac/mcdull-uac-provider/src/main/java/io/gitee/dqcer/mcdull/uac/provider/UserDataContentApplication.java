package io.gitee.dqcer.mcdull.uac.provider;

import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableFeignClientsScan;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableMapperScan;
import io.gitee.dqcer.mcdull.uac.provider.config.Ip2RegionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * 身份验证应用程序
 *
 * @author dqcer
 * @since 2022/10/31
 */
@EnableWebCore
@EnableMapperScan
@EnableFeignClientsScan
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class UserDataContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDataContentApplication.class, args);
        SpringApplication application = new SpringApplication(UserDataContentApplication.class);
        application.addListeners(new Ip2RegionListener());
        application.run(args);
    }
}
