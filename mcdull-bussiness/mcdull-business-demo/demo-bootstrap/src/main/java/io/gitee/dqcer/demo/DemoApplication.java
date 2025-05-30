package io.gitee.dqcer.demo;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFileStorage
@EnableWebCore
@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE})
@EnableDynamicDataSource
@EnableCache
@SpringBootApplication(scanBasePackages = "io.gitee")
public class DemoApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(DemoApplication.class, args);
    }
}

