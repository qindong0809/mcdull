package com.dqcer.mcdull.mdc.provider;

import com.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.dqcer.mcdull.framework.web.remote"})
@MapperScan("com.dqcer.mcdull.mdc.provider.web.dao.mapper")
@EnableWebCore
@EnableDiscoveryClient
@SpringBootApplication
public class MetaDataContentApplication {


    public static void main(String[] args) {
        SpringApplication.run(MetaDataContentApplication.class, args);
    }
}