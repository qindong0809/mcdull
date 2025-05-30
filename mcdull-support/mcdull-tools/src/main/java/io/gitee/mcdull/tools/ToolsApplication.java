package io.gitee.mcdull.tools;

import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFileStorage
@EnableCache
@SpringBootApplication(scanBasePackages = "io.gitee")
public class ToolsApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ToolsApplication.class, args);
    }
}
