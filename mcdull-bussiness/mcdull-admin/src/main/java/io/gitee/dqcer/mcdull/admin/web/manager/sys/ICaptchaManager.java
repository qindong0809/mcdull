package io.gitee.dqcer.mcdull.admin.web.manager.sys;


import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;

/**
 * 验证码 通用逻辑定义
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface ICaptchaManager {

    /**
     * 验证 验证码
     *
     * @param code 代码
     * @param uuid uuid
     * @return boolean
     */
    boolean validateCaptcha(String code, String uuid);

    /**
     * 构建验证码
     *
     * @return {@link KeyValueBO}<{@link String}, {@link String}>
     */
    KeyValueBO<String, String> builderCaptcha();
}
