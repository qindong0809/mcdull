package io.gitee.dqcer.mcdull.admin.web.service.common;

import io.gitee.dqcer.mcdull.admin.model.vo.common.CaptchaVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
* 验证码 业务接口类
*
* @author dqcer
* @since 2023-01-19
*/
public interface ICaptchaService {

    /**
     * 获得登录验证码
     *
     * @return {@link Result}<{@link CaptchaVO}>
     */
    Result<CaptchaVO> getLoginCode();


}
