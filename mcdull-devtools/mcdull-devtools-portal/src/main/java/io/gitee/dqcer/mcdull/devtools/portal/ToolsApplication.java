package io.gitee.dqcer.mcdull.devtools.portal;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableFileStorage
@SpringBootApplication(
    scanBasePackages = "io.gitee.dqcer.mcdull.tools",
    exclude = {
        DataSourceAutoConfiguration.class
    }
)
public class ToolsApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ToolsApplication.class, args);
    }
}
