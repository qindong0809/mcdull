package io.gitee.dqcer.mcdull.uac.provider.config;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.MessageTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMessageService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IPasswordPolicyService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Date;

/**
 * Sa-token 令牌侦听器
 *
 * @author dqcer
 * @since 2024/11/25
 */
@Component
public class MySaTokenListener implements SaTokenListener {

    @Resource
    private IUserService userService;
    
    @Resource
    private ICommonManager commonManager;

    @Resource
    private IMessageService messageService;

    @Resource
    private IPasswordPolicyService passwordPolicyService;

    @Resource
    private ILoginLogService loginLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        Integer userId = (Integer) loginId;
        UserEntity user = userService.get(userId);
        if (ObjUtil.isNotNull(user)) {
            Date lastLoginTime = user.getLastLoginTime();
            if (ObjUtil.isNull(lastLoginTime)) {
                String welcome = commonManager.getConfig("first-login-send-message");
                if (StrUtil.isNotBlank(welcome)) {
                    messageService.insert(MessageTypeEnum.MAIL, userId, userId.toString(), "欢迎使用", welcome);
                }
            }
            userService.updateLoginTime(userId);
            PasswordPolicyVO passwordPolicy = passwordPolicyService.detail();
            if (ObjUtil.isNotNull(passwordPolicy)) {
                Integer passwordExpiredPeriod = passwordPolicy.getPasswordExpiredPeriod();
                if (ObjUtil.isNotNull(passwordExpiredPeriod)) {
                    LoginLogEntity firstLoginLog = loginLogService.getFirstLoginLog(user.getLoginName());
                    if (ObjUtil.isNotNull(firstLoginLog)) {
                        Date createdTime = firstLoginLog.getCreatedTime();
                        long time = (new Date().getTime() - createdTime.getTime()) / 1000 / 60;
                        if (time > passwordExpiredPeriod) {
                            String reminderTemplate = commonManager.getConfig("expired-password-reminder");
                            if (StrUtil.isNotBlank(reminderTemplate)) {
                                String config = commonManager.getConfig("expired-password-reminder-frequency");
                                if (StrUtil.isNotBlank(config)) {
                                    Integer frequency = Convert.toInt(config);
                                    String formatted = StrUtil.format("{}（有效期{}）",
                                    DateUtil.formatDate(DateUtil.offsetDay(createdTime, passwordExpiredPeriod)) , passwordExpiredPeriod);
                                    String reminder = StrUtil.format(reminderTemplate, formatted);
                                    if (ObjUtil.equal(frequency, GlobalConstant.Number.NUMBER_0)) {
                                        return;
                                    }
                                    String title = "密码过期提醒";
                                    if (ObjUtil.equal(frequency, GlobalConstant.Number.NUMBER_1)) {
                                        boolean exist = messageService.getByUserId(userId, userId.toString());
                                        if (!exist) {
                                            messageService.insert(MessageTypeEnum.MAIL, userId, userId.toString(), title, reminder);
                                        }
                                    }
                                    if (ObjUtil.equal(frequency, GlobalConstant.Number.NUMBER_2)) {
                                        messageService.insert(MessageTypeEnum.MAIL, userId, userId.toString(), title, reminder);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {

    }

    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {

    }

    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {

    }

    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }

    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }
}
