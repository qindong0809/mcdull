package io.gitee.dqcer.admin;

import io.gitee.dqcer.admin.framework.EnableMapperScan;
import io.gitee.dqcer.framework.redis.EnableCache;
import io.gitee.dqcer.framework.web.EnableWebCore;
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
