package io.gitee.dqcer;

import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableMapperScan;
import io.gitee.dqcer.mcdull.uac.provider.config.Ip2RegionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@EnableWebCore
@EnableMapperScan
@EnableCache
@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
//        SpringApplication.run(UserDataContentApplication.class, args);
        SpringApplication application = new SpringApplication(Main.class);
        application.addListeners(new Ip2RegionListener());
        application.run(args);
    }
}