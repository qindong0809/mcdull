package io.gitee.dqcer.mcdull.uac.provider.flow.login;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLockedService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component(value = "ValidateLoginNamePassword")
@TreeNode(code = "Validate Login Name Password")
public class ValidateLoginNamePassword implements ProcessHandler<LoginContext> {

    @Resource
    private IUserService userService;

    @Resource
    private ILoginLockedService loginLockedService;

    @Override
    public void execute(LoginContext context) {
        LoginDTO dto = context.getLoginDTO();
        String username = dto.getLoginName();
        String passwordDTO = dto.getPassword();
        UserEntity userEntity = userService.get(username);
        if (ObjUtil.isNotNull(userEntity)) {
            boolean isOk = userService.passwordCheck(userEntity, passwordDTO);
            if (isOk) {
                if (Boolean.TRUE.equals(userEntity.getInactive())) {
                    throw new BusinessException("user.account.not.active");
                }
                LoginLockedEntity lockedEntity = loginLockedService.get(username);
                if (ObjUtil.isNotNull(lockedEntity)) {
                    if (BooleanUtil.isTrue(lockedEntity.getLockFlag())) {
                        if (DateUtil.compare(lockedEntity.getLoginLockEndTime(), new Date()) > 0) {
                            loginLockedService.updateFailCount(lockedEntity);
                            throw new BusinessException("user.account.locked");
                        }
                    }
                }
                Dict dict = Dict.create();
                dict.put("user", userEntity);
                context.setDict(dict);
                return;
            }
        }
        throw new BusinessException("incorrect.username.or.password");
    }

    @Override
    public boolean extendParentNodeTryAttr() {
        return true;
    }
}
