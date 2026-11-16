package io.gitee.dqcer.mcdull.mdc.client.constants;

/**
 * mdc 契约常量
 * <p>
 * 与 {@code SystemConstant} 同理：服务名是调用方与服务方必须一致的线上契约，
 * 随 mcdull-mdc-client 的版本一起演进，不要下放成各服务的配置项。
 *
 * @author dqcer
 * @since 2026-11-16
 */
public final class MdcConstant {

    /**
     * mdc 元数据服务在注册中心的服务名，取自 mcdull-mdc-provider 的 spring.application.name
     */
    public static final String SERVICE_NAME = "mcdull-mdc-provider";

    private MdcConstant() {
    }
}
