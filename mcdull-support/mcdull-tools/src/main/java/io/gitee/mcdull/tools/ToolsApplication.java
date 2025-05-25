package io.gitee.mcdull.tools;

import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFileStorage
//@EnableWebCore
//@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE})
//@EnableDynamicDataSource
@EnableCache
@SpringBootApplication(scanBasePackages = "io.gitee")
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class ToolsApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ToolsApplication.class, args);
    }
}
