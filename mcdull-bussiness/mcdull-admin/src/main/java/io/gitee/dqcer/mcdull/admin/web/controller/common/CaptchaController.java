package io.gitee.dqcer.mcdull.admin.web.controller.common;

import io.gitee.dqcer.mcdull.admin.model.vo.common.CaptchaVO;
import io.gitee.dqcer.mcdull.admin.web.service.common.ICaptchaService;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 验证码控制器
 *
 * @author dqcer
 * @since 2023/03/11
 */
@RestController
public class CaptchaController {

    @Resource
    private ICaptchaService captchaService;

    @UnAuthorize
    @GetMapping("/captchaImage")
    public Result<CaptchaVO> getCode() {
        return captchaService.getLoginCode();
    }

}
