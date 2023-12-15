package io.gitee.dqcer.mcdull.admin.web.service.common.impl;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.common.CaptchaVO;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.ICaptchaManager;
import io.gitee.dqcer.mcdull.admin.web.service.common.ICaptchaService;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService {

    private static final Logger log = LoggerFactory.getLogger(CaptchaServiceImpl.class);
    @Resource
    private ISysConfigManager sysConfigManager;

    @Resource
    private ICaptchaManager captchaManager;


    @Override
    public Result<CaptchaVO> getLoginCode() {
        CaptchaVO vo = new CaptchaVO();

        String valueByEnum = sysConfigManager.findValueByEnum(SysConfigKeyEnum.CAPTCHA);
        boolean enable = Convert.toBool(valueByEnum);
        vo.setCaptchaEnabled(enable);
        if (!enable) {
            log.info("未开启登录验证码功能");
            return Result.success(vo);
        }

        KeyValueBO<String, String> bo = captchaManager.builderCaptcha();
        vo.setImg(bo.getValue());
        vo.setUuid(bo.getKey());

        return Result.success(vo);
    }
}
