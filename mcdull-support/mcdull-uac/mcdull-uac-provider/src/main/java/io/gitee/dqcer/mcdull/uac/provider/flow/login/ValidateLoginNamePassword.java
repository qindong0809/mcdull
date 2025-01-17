package io.gitee.dqcer.mcdull.uac.provider.flow.login;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLockedService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IPasswordPolicyService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Date;

@Component(value = "ValidateLoginNamePassword")
@TreeNode(code = "Validate Login Name Password")
public class ValidateLoginNamePassword implements ProcessHandler<LoginContext> {

    @Resource
    private IUserService userService;

    @Resource
    private ILoginLockedService loginLockedService;

    @Resource
    private IPasswordPolicyService passwordPolicyService;

    @Override
    public void execute(LoginContext context) {
        LoginDTO dto = context.getLoginDTO();
        String username = dto.getLoginName();
        String passwordDTO = dto.getPassword();
        UserEntity userEntity = userService.get(username);
        PasswordPolicyVO policyVO = passwordPolicyService.detail();
        Integer max = policyVO.getFailedLoginMaximumNumber();
        int failCount = loginLockedService.getLoginFailureCount(username) + 1;
        int okCount = max - failCount;
        this.checkOkCount(okCount, username);

        if (ObjUtil.isNotNull(userEntity)) {
            boolean isOk = userService.passwordCheck(userEntity, passwordDTO);
            if (isOk) {
                if (Boolean.TRUE.equals(userEntity.getInactive())) {
                    throw new BusinessException("user.account.not.active");
                }
                loginLockedService.inactive(username);
                Dict dict = Dict.create();
                dict.put("user", userEntity);
                context.setDict(dict);
                return;
            }
        }
        Integer failedLoginMaximumTime = policyVO.getFailedLoginMaximumTime();
        loginLockedService.updateFailCount(username, failedLoginMaximumTime, failCount);
        this.checkOkCount(okCount, username);
        throw new BusinessException("incorrect.username.or.password", new Object[]{okCount, failedLoginMaximumTime});
    }

    private void checkOkCount(int okCount, String username) {
        if (okCount <= 0) {
            LoginLockedEntity loginLocked = loginLockedService.get(username);
            Date loginLockEndTime = loginLocked.getLoginLockEndTime();
            if (DateUtil.compare(loginLockEndTime, new Date()) > 0) {
                long between = DateUtil.between(new Date(), loginLockEndTime, DateUnit.MINUTE);
                throw new BusinessException("user.account.locked", new Object[]{between});
            }
        }
    }

    @Override
    public boolean extendParentNodeTryAttr() {
        return true;
    }
}
