package io.gitee.dqcer.mcdull.system;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.system.provider.config.Ip2RegionListener;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableFileStorage
@EnableWebCore
@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE, GlobalConstant.MAPPER_PACKAGE_DAO})
@EnableDynamicDataSource
@EnableCache
@SpringBootApplication(scanBasePackages = "io.gitee")
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class SystemApplication {
    public static void main( String[] args ) {
        SpringApplication application = new SpringApplication(SystemApplication.class);
        application.addListeners(new Ip2RegionListener());
        application.run(args);
    }
}
