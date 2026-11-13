package io.gitee.dqcer.mcdull.framework.feign.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 双模式 RPC 契约标记注解
 * <p>
 * 组合 {@link FeignClient}，让同一份接口契约在两种部署形态下自动切换：
 * <ul>
 *     <li>单体模式（本地实现存在）：直接走 JVM 内存调用，不初始化 Feign 客户端；</li>
 *     <li>微服务模式（本地实现不在 classpath）：激活 Feign 代理，走 HTTP 调用。</li>
 * </ul>
 * 实现类上再加 {@code @Primary} 与 {@code @RestController}，即可同时满足本地注入与远程暴露。
 *
 * <pre>{@code
 * @RpcClient(value = "mcdull-system", path = "/interior-def/system/user")
 * public interface UserApiDef {
 *     @GetMapping("/{userId}")
 *     UserVO get(@PathVariable("userId") Integer userId);
 * }
 * }</pre>
 *
 * @author dqcer
 * @since 2024/06/12
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
