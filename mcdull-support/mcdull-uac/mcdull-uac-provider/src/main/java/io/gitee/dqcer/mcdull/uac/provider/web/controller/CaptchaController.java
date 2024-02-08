package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Producer;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * 验证码操作处理
 *
 * @author dqcer
 */
@Tag(name = "验证码相关")
@RestController
public class CaptchaController {

    public static final String CAPTCHA = "captcha_codes:";

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    private RedissonCache redissonCache;


    @SaIgnore
    @Operation(summary = "获取验证码", description = "不需要登录， 验证时需要传回UUID。默认有效期为2分钟")
    @GetMapping("/captchaImage")
    public ResultWrapper getCode() {
        ResultWrapper resultWrapper = ResultWrapper.success();
        // 保存验证码信息
        String uuid = RandomUtil.randomString(32);
        String verifyKey = CAPTCHA + uuid;

        // 生成验证码
        String capText = captchaProducerMath.createText();
        String capStr = capText.substring(0, capText.lastIndexOf(StrUtil.AT));
        String code = capText.substring(capText.lastIndexOf(StrUtil.AT) + 1);
        BufferedImage image = captchaProducerMath.createImage(capStr);

        redissonCache.put(verifyKey, code, 2 * 60);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return ResultWrapper.error(e.getMessage());
        }
        resultWrapper.put("uuid", uuid);
        resultWrapper.put("img", Base64.encode(os.toByteArray()));
        return resultWrapper;
    }
}
