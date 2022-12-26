package com.dqcer.mcdull.uac.provider.config;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * feign 配置
 *
 * @author dqcer
 * @date 2022/07/25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableUacFeignClients {

    @SuppressWarnings("unused")
    String[] basePackages() default {"com.dqcer.mcdull.*.client.service", "com.dqcer.mcdull.framework.web.feign"};


}