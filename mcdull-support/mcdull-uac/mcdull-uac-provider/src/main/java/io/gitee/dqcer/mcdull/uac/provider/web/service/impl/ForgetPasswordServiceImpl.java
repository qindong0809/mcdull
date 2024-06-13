package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.temp.SaTempUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRequestDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRestDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IConfigService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IForgetPasswordService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * forget password service impl
 *
 * @author dqcer
 * @since 2024/06/11
 */
@Service
public class ForgetPasswordServiceImpl implements IForgetPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ForgetPasswordServiceImpl.class);

    @Resource
    private IUserService userService;

    @Resource
    private IConfigService configService;

    @Resource
    private IEmailService emailService;

    @Override
    public String request(ForgetPasswordRequestDTO dto) {
        String userIdentity = dto.getUserIdentity();
        UserEntity user = userService.get(userIdentity);
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException("user.not.found", ArrayUtil.wrap(userIdentity));
        }
        String timeoutStr = configService.getConfig("forget-password-timeout");
        String domainName = configService.getConfig("domain-name");
        String systemName = configService.getConfig("system-name");
        String forgetPasswordEmailTitle = configService.getConfig("forget-password-email-title");
        String token = SaTempUtil.createToken(user.getId(), Convert.toInt(timeoutStr) * 60);
        MapBuilder<String, String> builder = MapUtil.builder();
        builder
                .put("{actualName}", user.getActualName())
                .put("{systemName}", systemName)
                .put("{domainName}", domainName)
                .put("{link}", "rest-update-password/" + token)
                .put("{timeout}", timeoutStr);
        String content = null;
        String templateFileName = "template/forget-password-email.html";
        try (InputStream inputStream = ResourceUtil.getStream(templateFileName)) {
            if (inputStream != null) {
                byte[] bytes = IoUtil.readBytes(inputStream);
                content = new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            LogHelp.error(log, "模版文件: {}", templateFileName, e);
            throw new BusinessException("找不到对应的模版文件");
        }
        emailService.sendEmailHtml(user.getEmail(), forgetPasswordEmailTitle,
                this.replacePlaceholders(content, builder.map()));
        return token;
    }

    @Override
    public void reset(Integer userId, ForgetPasswordRestDTO dto) {
        String token = dto.getToken();
        // 获取指定 token 的剩余有效期，单位：秒
        long timeout = SaTempUtil.getTimeout(token);
        if (timeout <= 0) {
            throw new BusinessException("链接已过期");
        }
        userService.resetPassword(userId, dto.getNewPassword());
        SaTempUtil.deleteToken(token);
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }

}
