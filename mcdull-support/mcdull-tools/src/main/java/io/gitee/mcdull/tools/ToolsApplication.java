package io.gitee.mcdull.tools;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableFileStorage
@SpringBootApplication(
    scanBasePackages = "io.gitee.mcdull.tools",
    exclude = {
        DataSourceAutoConfiguration.class
    }
)
public class ToolsApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ToolsApplication.class, args);
    }
}
