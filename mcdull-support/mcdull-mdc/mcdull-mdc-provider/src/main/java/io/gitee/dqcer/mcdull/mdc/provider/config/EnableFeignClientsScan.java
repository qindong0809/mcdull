package io.gitee.dqcer.mcdull.mdc.provider.config;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * feign 配置
 *
 * @author dqcer
 * @since 2022/07/25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableFeignClientsScan {

    @SuppressWarnings("unused")
    String[] basePackages() default {
        GlobalConstant.BASE_PACKAGE + ".framework.web.feign",
            GlobalConstant.BASE_PACKAGE + ".*.client.service"
    };
}