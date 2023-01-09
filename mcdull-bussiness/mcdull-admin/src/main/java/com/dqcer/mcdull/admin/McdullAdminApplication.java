package com.dqcer.mcdull.admin;

import com.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import com.dqcer.mcdull.framework.redis.EnableCache;
import com.dqcer.mcdull.framework.web.EnableWebCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 麦兜管理应用程序
 *
 * @author QinDong
 * @date 2022/12/26
 */
@EnableWebCore
//@EnableDiscoveryClient
@EnableDynamicDataSource
//@EnableFeignClients(basePackages = {"com.dqcer.mcdull.framework.web.feign"})
@EnableCache
@SpringBootApplication
public class McdullAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(McdullAdminApplication.class, args);
    }

}
