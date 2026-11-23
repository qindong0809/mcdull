package io.gitee.dqcer.mcdull.demo;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * demo 服务启动入口。
 */
@EnableWebCore
@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE, GlobalConstant.MAPPER_PACKAGE_DAO})
@EnableDynamicDataSource
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "io.gitee")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
