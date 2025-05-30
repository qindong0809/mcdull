package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedisClient;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.framework.web.config.SystemEnvironment;
import io.gitee.dqcer.mcdull.system.provider.model.vo.CaptchaVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICaptchaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class CaptchaServiceImpl
        extends GenericLogic implements ICaptchaService {

    @Resource
    private DefaultKaptcha defaultKaptcha;
    @Resource
    private RedisClient redisClient;
    @Resource
    private SystemEnvironment systemEnvironment;

    private static final String CAPTCHA_KEY = "login_captcha:{}";
    private static final int EXPIRE_SECOND = 65;

    @Override
    public CaptchaVO get() {
        String captchaText = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(captchaText);
        String base64Code;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            base64Code = Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) {
            LogHelp.error(log, "generateCaptcha error:", e);
            throw new BusinessException("生成验证码错误");
        }
        String uuid = UUID.randomUUID().toString().replace("-", StrUtil.EMPTY);
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaUuid(uuid);
        captchaVO.setCaptchaBase64Image("data:image/png;base64," + base64Code);
        captchaVO.setExpireSeconds(EXPIRE_SECOND);
        if (!systemEnvironment.getProd()) {
            captchaVO.setCaptchaText(captchaText);
        }
        String key = StrUtil.format(CAPTCHA_KEY, uuid);
        redisClient.set(key, captchaText, EXPIRE_SECOND);
        return captchaVO;

    }

    @Override
    public void checkCaptcha(String code, String uuid) {
        String key = StrUtil.format(CAPTCHA_KEY, uuid);
        String redisCaptchaCode = redisClient.get(key);
        if (StrUtil.isBlank(redisCaptchaCode) || !StrUtil.equals(redisCaptchaCode, code)) {
            throw  new BusinessException("验证码错误或已过期，请输入正确的验证码或刷新后重新输入");
        }
        // 删除已使用的验证码
        redisClient.delete(key);
    }
}
