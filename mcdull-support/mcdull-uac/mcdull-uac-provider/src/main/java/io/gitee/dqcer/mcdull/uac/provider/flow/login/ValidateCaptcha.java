package io.gitee.dqcer.mcdull.uac.provider.flow.login;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.enums.EnvironmentEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.web.config.SystemEnvironment;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;
import io.gitee.dqcer.mcdull.uac.provider.util.Ip2RegionUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "ValidateCaptcha")
@TreeNode(code = "Validate Captcha")
public class ValidateCaptcha implements ProcessHandler<LoginContext> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SystemEnvironment systemEnvironment;

    @Resource
    private ICaptchaService captchaService;

    @Resource
    private ILoginLogService loginLogService;

    @Resource
    private IPasswordPolicyService passwordPolicyService;

    @Resource
    private CacheChannel cacheChannel;

    @Resource
    private ILoginLockedService loginLockedService;

    @Resource
    private IUserService userService;

    @Override
    public void execute(LoginContext context) {
        LoginDTO dto = context.getLoginDTO();
        if (!EnvironmentEnum.DEV.getCode().equals(systemEnvironment.getEnvironment())) {
            captchaService.checkCaptcha(dto.getCaptchaCode(), dto.getCaptchaUuid());
        }
    }

    @Override
    public void catchException(Exception exception, LoginContext context) {
        ProcessHandler.super.catchException(exception, context);
        Dict dict = context.getDict();
        if (ObjUtil.isNull(dict)) {
            dict = Dict.create();
        }
        dict.put("typeEnum", LoginLogResultTypeEnum.LOGIN_FAIL);
        context.setDict(dict);
    }

    @Override
    public void finallyException(ProcessHandler processHandler, Exception exception, LoginContext context) {
        if (processHandler.endNode() || ObjUtil.isNotNull(exception)) {
            ProcessHandler.super.finallyException(processHandler, exception, context);
            LoginDTO dto = context.getLoginDTO();
            Dict dict = context.getDict();
            LoginLogResultTypeEnum typeEnum = (LoginLogResultTypeEnum) dict.get("typeEnum");
            String message = ObjUtil.isNotNull(exception) ? exception.getMessage() : StrUtil.EMPTY;
            this.saveLoginLog(dto.getLoginName(), typeEnum, message);
            this.passwordPolicyHandle(dto, typeEnum,  dict.get("user", new UserEntity()), dto.getLoginName());
        }
    }

    private void passwordPolicyHandle(LoginDTO dto, LoginLogResultTypeEnum typeEnum, UserEntity userEntity, String loginName) {
        String failKey = StrUtil.format("user_login_fail:{}", dto.getLoginName());

        if (LoginLogResultTypeEnum.LOGIN_FAIL == typeEnum) {
            PasswordPolicyVO policyVO = passwordPolicyService.detail();

            Object obj = cacheChannel.get(failKey, Object.class);
            Integer failCount = 0;
            if (ObjUtil.isNull(obj)) {
                failCount = Convert.toInt(obj, 0);
            }
            if (failCount > policyVO.getFailedLoginMaximumNumber()) {
                loginLockedService.lock(loginName, policyVO.getFailedLoginMaximumTime());
                throw new BusinessException("user.account.locked");
            }
            cacheChannel.put(failKey, failCount + 1, policyVO.getFailedLoginMaximumTime() * 60);
        }
        if (LoginLogResultTypeEnum.LOGIN_SUCCESS == typeEnum) {
            cacheChannel.evict(failKey);
            userService.updateLoginTime(userEntity.getId());
        }
    }

    private void saveLoginLog(String loginName, LoginLogResultTypeEnum resultTypeEnum, String remark) {
        LoginLogEntity log = new LoginLogEntity();
        log.setLoginName(loginName);
        String ipAddr = IpUtil.getIpAddr(ServletUtil.getRequest());
        log.setLoginIp(ipAddr);
        log.setLoginIpRegion(Ip2RegionUtil.getRegion(ipAddr));
        log.setLoginResult(resultTypeEnum.getCode());
        log.setUserAgent(ServletUtil.getUserAgent());
        log.setRemark(remark);
        loginLogService.add(log);
    }
}
