package io.gitee.dqcer.mcdull.workflow.facade;

import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 以远程方式接入 workflow 契约。
 * <p>
 * 微服务拆分部署时由消费方启动类显式声明；单体合并部署时不要加该注解，直接注入
 * {@code @Primary} 的本地实现即可。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients(basePackages = WorkflowConstant.CLIENT_BASE_PACKAGE)
public @interface EnableWorkflowFeignClients {
}
