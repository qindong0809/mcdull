package io.gitee.dqcer;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.system.provider.config.Ip2RegionListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@EnableWebCore
//@EnableMapperScan(basePackages = {GlobalConstant.BASE_PACKAGE + ".uac.provider.web.dao.mapper",
//         "io.gitee.dqcer.blaze.dao.mapper", })
@MapperScan(basePackages = {GlobalConstant.BASE_PACKAGE + ".**.mapper",})
//@EnableMapperScan
@EnableDynamicDataSource
@EnableCache
@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.addListeners(new Ip2RegionListener());
        application.run(args);

    }
}