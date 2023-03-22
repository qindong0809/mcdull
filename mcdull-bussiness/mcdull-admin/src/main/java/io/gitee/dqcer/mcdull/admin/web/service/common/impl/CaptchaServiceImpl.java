package io.gitee.dqcer.mcdull.admin.web.service.common.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Producer;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.common.CaptchaVO;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.admin.web.service.common.ICaptchaService;
import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService {

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    private static final Logger log = LoggerFactory.getLogger(CaptchaServiceImpl.class);
    @Resource
    private ISysConfigManager sysConfigManager;

    @Resource
    private RedissonCache redissonCache;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Override
    public Result<CaptchaVO> getLoginCode() {
        CaptchaVO vo = new CaptchaVO();

        String valueByEnum = sysConfigManager.findValueByEnum(SysConfigKeyEnum.CAPTCHA);
        boolean enable = Convert.toBool(valueByEnum);
        vo.setCaptchaEnabled(enable);
        if (!enable) {
            log.info("未开启登录验证码功能");
            return Result.ok(vo);
        }

        String mathText = captchaProducerMath.createText();
        String capStr = mathText.substring(0, mathText.lastIndexOf(SymbolConstants.AT));

        String code = mathText.substring(mathText.lastIndexOf(SymbolConstants.AT) + 1);

        BufferedImage image = captchaProducerMath.createImage(capStr);

        String uuid = RandomUtil.uuid();
        redissonCache.put(CAPTCHA_CODE_KEY + uuid, code, 2 * 60);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();

        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new BusinessException(e);
        }

        vo.setImg(Base64.encode(os.toByteArray()));
        vo.setUuid(uuid);

        return Result.ok(vo);
    }

    @Override
    public boolean validateCaptcha(String code, String uuid) {
        String valueByEnum = sysConfigManager.findValueByEnum(SysConfigKeyEnum.CAPTCHA);
        boolean enable = Convert.toBool(valueByEnum);
        enable = false;
        if (!enable) {
            return true;
        }
        if (StrUtil.isBlank(code) || StrUtil.isBlank(uuid)) {
            throw new IllegalArgumentException();
        }
        String cacheCode = redissonCache.get(CAPTCHA_CODE_KEY + uuid, String.class);
        if (StrUtil.isBlank(cacheCode)) {
            throw new BusinessException("验证码过期");
        }
        return code.equals(cacheCode);
    }
}
