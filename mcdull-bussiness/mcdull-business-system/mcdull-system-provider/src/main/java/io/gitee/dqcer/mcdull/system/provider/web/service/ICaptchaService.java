package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.vo.CaptchaVO;

/**
 * Captcha service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface ICaptchaService {

    CaptchaVO get();

    void checkCaptcha(String code, String uuid);
}
