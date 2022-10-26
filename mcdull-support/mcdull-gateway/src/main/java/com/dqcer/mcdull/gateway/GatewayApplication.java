package com.dqcer.mcdull.gateway;

import com.dqcer.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关应用程序
 *
 * @author dongqin
 * @date 2022/10/24
 */
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    private static final Logger log = LoggerFactory.getLogger(GatewayApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @GetMapping
    public Result<String> demo() {
        return Result.ok("good job");
    }

    @GetMapping("log")
    public Result<String> testLog() {
        log.error("2223");
        return Result.ok("testLog");
    }
}
