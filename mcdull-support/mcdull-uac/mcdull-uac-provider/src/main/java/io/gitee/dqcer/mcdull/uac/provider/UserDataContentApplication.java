package io.gitee.dqcer.mcdull.uac.provider;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.gitee.dqcer.mcdull.framework.redis.EnableCache;
import io.gitee.dqcer.mcdull.framework.web.EnableWebCore;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableMapperScan;
import io.gitee.dqcer.mcdull.uac.provider.config.EnableFeignClientsScan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 身份验证应用程序
 *
 * @author dqcer
 * @since 2022/10/31
 */
@EnableWebCore
@EnableMapperScan
@EnableFeignClientsScan
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
@Tag(name = "首页模块")
@RestController
public class UserDataContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDataContentApplication.class, args);
    }

    @Parameter(name = "name",required = true)
    @ApiOperationSupport(author = "chengsf")//author接口添加作者，order排序，接口展示顺序
    @Operation(description  = "向客人问好")
    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi(@RequestParam(value = "name")String name){
        return ResponseEntity.ok("Hi:"+name);
    }
}
