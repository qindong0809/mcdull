package com.dqcer.mcdull.admin;

import com.dqcer.mcdull.admin.config.EnableMapperScan;
import com.dqcer.mcdull.framework.redis.EnableCache;
import com.dqcer.mcdull.framework.web.EnableWebCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 麦兜管理应用程序
 *
 * @author dqcer
 * @date 2022/12/26
 */
@EnableWebCore
@EnableMapperScan
@EnableCache
@SpringBootApplication
public class McdullAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(McdullAdminApplication.class, args);
    }

}
