package com.dqcer.mcdull.uac.provider.config;

import java.lang.annotation.*;

/**
 * feign 配置
 *
 * @author dqcer
 * @date 2022/07/25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableUacFeignClients {

    @SuppressWarnings("unused")
    String[] basePackages() default {"com.dqcer.mcdull.framework.web.feign.service", "com.dqcer.mcdull.uac.client", "com.dqcer.mcdull.mdc.client"};


}