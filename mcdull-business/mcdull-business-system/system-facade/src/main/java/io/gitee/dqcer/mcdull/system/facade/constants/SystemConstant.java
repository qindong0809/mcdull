package io.gitee.dqcer.mcdull.system.facade.constants;

/**
 * system 契约常量
 * <p>
 * 这里的值都是调用方与服务方必须达成一致的线上契约，随 system-facade 的版本一起演进。
 * 不要改成配置项：两侧各自解析配置会引入漂移，而漂移的表现是静默 404。
 *
 * @author dqcer
 * @since 2026-11-16
 */
public final class SystemConstant {

    /**
     * system 能力在注册中心的服务名，取自 system-bootstrap 的 spring.application.name
     */
    public static final String SERVICE_NAME = "system-bootstrap";

    /**
     * 契约接口所在包，供消费方启用 Feign 代理时引用
     * <p>
     * 取 {@code service} 而不是 {@code service.def}：{@code @EnableFeignClients} 只向下递归扫描，
     * 直接声明 {@code @FeignClient} 的 {@code AuthApi}、{@code ConfigApi} 在父包上，写成子包会漏掉它们。
     */
    public static final String CLIENT_BASE_PACKAGE = "io.gitee.dqcer.mcdull.system.facade.service";

    private SystemConstant() {
    }
}
