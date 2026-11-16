package io.gitee.dqcer.mcdull.framework.feign.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC 契约标记注解
 * <p>
 * 组合 {@link FeignClient}，只暴露契约需要的几个属性；目标服务名由各 facade 模块自己的常量类给出，
 * 框架层不需要知道任何业务包名。
 * <p>
 * 单体还是微服务部署，由消费方的启动类显式决定，而不是靠运行时配置开关：
 * <ul>
 *     <li>单体合并部署（启动包内含实现模块）：不启用 Feign 客户端扫描，注入命中的是标了
 *         {@code @Primary} 的本地实现，走 JVM 内存调用；</li>
 *     <li>微服务拆分部署（启动包内只有契约）：由 facade 模块自带的 {@code Enable*FeignClients}
 *         注解激活 Feign 代理，走 HTTP 调用。</li>
 * </ul>
 * 实现类上同时标 {@code @Primary} 与 {@code @RestController}，两种形态共用同一份实现。
 * <p>
 * 同一个 facade 模块里的多个契约共用一个服务名，必须各自给出不同的 {@code contextId}：否则 Feign
 * 会为它们注册同一个配置占位 bean，远程部署的服务在启动阶段就抛 {@code BeanDefinitionOverrideException}。
 *
 * <pre>{@code
 * @RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "UserApi")
 * public interface UserApiDef {
 *     @GetMapping("/{userId}")
 *     UserVO get(@PathVariable("userId") Integer userId);
 * }
 * }</pre>
 *
 * @author dqcer
 * @since 2024-06-12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FeignClient
public @interface RpcClient {

    /**
     * 目标微服务名称，对应 {@link FeignClient#name()}
     */
    @AliasFor(annotation = FeignClient.class, attribute = "name")
    String value();

    /**
     * 同一个服务下多个 Feign 客户端的隔离标识
     */
    @AliasFor(annotation = FeignClient.class, attribute = "contextId")
    String contextId() default "";

    /**
     * 统一路径前缀，接口方法上可只写相对路径
     */
    @AliasFor(annotation = FeignClient.class, attribute = "path")
    String path() default "";

    /**
     * 单体模式下若本地实现与 Feign 代理同时存在，用 {@code false} 保证本地实现优先注入
     */
    @AliasFor(annotation = FeignClient.class, attribute = "primary")
    boolean primary() default false;

    /**
     * 远程调用失败时的降级实现
     */
    @AliasFor(annotation = FeignClient.class, attribute = "fallback")
    Class<?> fallback() default void.class;

}
