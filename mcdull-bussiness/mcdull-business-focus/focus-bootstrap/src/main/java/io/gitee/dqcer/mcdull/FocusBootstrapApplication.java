package io.gitee.dqcer.mcdull;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableFileStorage
@EnableWebCore
@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE, GlobalConstant.MAPPER_PACKAGE_DAO})
@EnableDynamicDataSource
@EnableCache
@SpringBootApplication(scanBasePackages = "io.gitee")
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class FocusBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(FocusBootstrapApplication.class, args);
    }
}
