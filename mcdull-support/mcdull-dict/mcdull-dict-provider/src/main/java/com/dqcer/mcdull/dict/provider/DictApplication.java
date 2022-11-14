package com.dqcer.mcdull.dict.provider;

import com.dqcer.mcdull.framework.web.EnableWebCore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.dqcer.mcdull.dict.provider.web.dao")
@EnableWebCore
@EnableDiscoveryClient
@SpringBootApplication
public class DictApplication {


    public static void main(String[] args) {
        SpringApplication.run(DictApplication.class, args);
    }
}
