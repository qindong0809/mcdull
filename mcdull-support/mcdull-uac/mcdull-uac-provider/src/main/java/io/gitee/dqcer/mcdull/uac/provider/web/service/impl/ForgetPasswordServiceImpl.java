package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.temp.SaTempUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRequestDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRestDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.EmailTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IForgetPasswordService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * forget password service impl
 *
 * @author dqcer
 * @since 2024/06/11
 */
@Service
public class ForgetPasswordServiceImpl
        extends GenericLogic implements IForgetPasswordService {

    @Resource
    private IUserService userService;

    @Resource
    private IEmailService emailService;

    @Resource
    private ICommonManager commonManager;

    @Override
    public String request(ForgetPasswordRequestDTO dto) {
        String userIdentity = dto.getUserIdentity();
        UserEntity user = userService.get(userIdentity);
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException("user.not.found", new Object[]{userIdentity});
        }
        String timeoutStr = commonManager.getConfig("forget-password-timeout");
        String domainName = commonManager.getConfig("domain-name");
        String systemName = commonManager.getConfig("system-name");
        String forgetPasswordEmailTitle = commonManager.getConfig("forget-password-email-title");
        String token = SaTempUtil.createToken(user.getId(), Convert.toInt(timeoutStr) * 60);
        MapBuilder<String, String> builder = MapUtil.builder();
        builder
                .put("{actualName}", user.getActualName())
                .put("{systemName}", systemName)
                .put("{domainName}", domainName)
                .put("{link}", "rest-update-password/" + token)
                .put("{timeout}", timeoutStr);
        String templateFileName = "template/forget-password-email.html";
        String content = commonManager.readTemplateFileContent(templateFileName);
        emailService.sendEmailHtml(EmailTypeEnum.FORGET_PASSWORD, user.getEmail(), forgetPasswordEmailTitle,
                commonManager.replacePlaceholders(content, builder.map()));
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
}
