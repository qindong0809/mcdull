package io.gitee.dqcer.mcdull.system.provider.web.manager;

/**
 * 系统配置只读契约。
 *
 * <p>从 CommonManager 中剥离，使其不再注入 ConfigService，
 * 打断 CommonManager ↔ ConfigService 的循环依赖。
 */
public interface ISystemConfigManager {

    String getConfig(String key);

    Boolean isCaptchaEnabled();

    Boolean getConfigToBool(String key);
}
