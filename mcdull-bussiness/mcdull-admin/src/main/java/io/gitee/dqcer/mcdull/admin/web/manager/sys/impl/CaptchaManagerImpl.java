package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Producer;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.ICaptchaManager;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码通用逻辑实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class CaptchaManagerImpl implements ICaptchaManager {

    /**
     * 验证码 redis key
     */
    private static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    @Resource
    private ISysConfigManager sysConfigManager;

    @Resource
    private CacheChannel cacheChannel;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Override
    public boolean validateCaptcha(String code, String uuid) {
        String valueByEnum = sysConfigManager.findValueByEnum(SysConfigKeyEnum.CAPTCHA);
        boolean enable = Convert.toBool(valueByEnum);
        if (!enable) {
            return true;
        }
        if (StrUtil.isBlank(code) || StrUtil.isBlank(uuid)) {
            throw new IllegalArgumentException();
        }
        String cacheCode = cacheChannel.get(CAPTCHA_CODE_KEY + uuid, String.class);
        if (StrUtil.isBlank(cacheCode)) {
            throw new BusinessException("验证码过期");
        }
        return code.equals(cacheCode);
    }

    @Override
    public KeyValueBO<String, String> builderCaptcha() {
        String mathText = captchaProducerMath.createText();
        String capStr = mathText.substring(0, mathText.lastIndexOf(SymbolConstants.AT));

        String code = mathText.substring(mathText.lastIndexOf(SymbolConstants.AT) + 1);

        BufferedImage image = captchaProducerMath.createImage(capStr);

        String uuid = RandomUtil.uuid();
        cacheChannel.put(CAPTCHA_CODE_KEY + uuid, code, 2 * 60);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();

        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return new KeyValueBO<String, String>()
                .setKey(uuid)
                .setValue(Base64.encode(os.toByteArray()));
    }
}
