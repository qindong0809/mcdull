package io.gitee.dqcer.mcdull.mdc.provider;

import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 元数据 应用程序
 *
 * @author dqcer
 * @since 2022/12/26
 */
@EnableDynamicDataSource
@EnableFeignClients(basePackages = {"io.gitee.dqcer.mcdull.framework.web.feign", "io.gitee.dqcer.uac.client.service"})
@MapperScan("io.gitee.dqcer.mdc.provider.web.dao.mapper")
@EnableWebCore
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
public class MetaDataContentApplication {


    public static void main(String[] args) {
        SpringApplication.run(MetaDataContentApplication.class, args);
    }
}
