package io.gitee.dqcer.mcdull.system.provider.flow.login;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.system.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICaptchaService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component(value = "ValidateCaptcha")
@TreeNode(code = "Validate Captcha")
public class ValidateCaptcha implements ProcessHandler<LoginContext> {

    @Resource
    private ICaptchaService captchaService;

    @Resource
    private DynamicLocaleMessageSource dynamicLocaleMessageSource;

    @Resource
    private ILoginService loginService;

    @Resource
    private ICommonManager commonManager;

    @Override
    public void execute(LoginContext context) {
        LoginDTO dto = context.getLoginDTO();
        if (commonManager.isCaptchaEnabled()) {
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

    @SuppressWarnings("all")
    @Override
    public void finallyException(ProcessHandler processHandler, Exception exception, LoginContext context) {
        if (processHandler.endNode() || ObjUtil.isNotNull(exception)) {
            ProcessHandler.super.finallyException(processHandler, exception, context);
            LoginDTO dto = context.getLoginDTO();
            Dict dict = context.getDict();
            LoginLogResultTypeEnum typeEnum = (LoginLogResultTypeEnum) dict.get("typeEnum");
            String message = StrUtil.EMPTY;
            if (ObjUtil.isNotNull(exception)) {
                BusinessException be = (BusinessException) exception;
                message = dynamicLocaleMessageSource.getMessage(be.getMessageCode(), be.getArgs());
            }
            loginService.saveLoginLog(dto.getLoginName(), typeEnum, message);
        }
    }
}