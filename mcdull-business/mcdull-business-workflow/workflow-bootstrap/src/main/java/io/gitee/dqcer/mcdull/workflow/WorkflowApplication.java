package io.gitee.dqcer.mcdull.workflow;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 工作流服务启动入口。
 */
@EnableWebCore
@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE, GlobalConstant.MAPPER_PACKAGE_DAO})
@EnableDynamicDataSource
@EnableCache
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "io.gitee")
public class WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }
}
