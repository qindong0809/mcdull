package io.gitee.dqcer.mcdull.gateway;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * 网关应用程序
 *
 * @author dqcer
 * @since 2022/10/24
 */
@EnableFeignClients(basePackages = "io.gitee.dqcer.mcdull.uac.client.service")
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    private static final Logger log = LoggerFactory.getLogger(GatewayApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    @GetMapping
    public Result<String> demo() {
        return Result.ok("good job");
    }

    private String s  ="1";

    /**
     * 测试 agent gc
     *
     * @return {@link Result}<{@link String}>
     */
    @GetMapping("log")
    public Result<String> testAgentGc() {
        int max = 10000;
        for (int i = 0; i < max; i++) {
            s += new String("dfd");
        }
        return Result.ok("testLog");
    }
}
