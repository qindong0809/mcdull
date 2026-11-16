package io.gitee.dqcer.mcdull.system.facade;

import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 以远程方式接入 system 契约
 * <p>
 * 由消费方的启动类显式声明，扫描范围由契约自己给出，框架不需要知道 system 的包名。
 * <p>
 * 单体合并部署（启动包内含 system-impl）不要加这个注解：本地实现带 {@code @Primary}，
 * 直接注入即可，注册 Feign 代理只会多出一层无用的 HTTP 调用。
 *
 * @author dqcer
 * @since 2026-11-16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients(basePackages = SystemConstant.CLIENT_BASE_PACKAGE)
public @interface EnableSystemFeignClients {
}
