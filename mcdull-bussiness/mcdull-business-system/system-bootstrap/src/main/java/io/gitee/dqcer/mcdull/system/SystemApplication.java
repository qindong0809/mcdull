package io.gitee.dqcer.mcdull.system;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.system.provider.config.Ip2RegionListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableWebCore
@MapperScan(basePackages = {GlobalConstant.MAPPER_PACKAGE})
@EnableDynamicDataSource
@EnableCache
@SpringBootApplication(scanBasePackages = "io.gitee")
public class SystemApplication {
    public static void main( String[] args ) {
        SpringApplication application = new SpringApplication(SystemApplication.class);
        application.addListeners(new Ip2RegionListener());
        application.run(args);
    }
}
