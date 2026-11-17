package io.gitee.dqcer.mcdull.system.provider.web.feign;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Primary
@RestController
public class AuthApiDefImpl implements AuthApiDef {

    @Resource
    private IUserService userService;
    @Resource
    private ILoginService loginService;

    @Override
    public InnerResult<RemoteLogonUserVO> getLogonUserInfo(Object loginId, String loginType) {
        LogHelp.info(log, "AuthApiDefImpl#getLogonUserInfo");
        RemoteLogonUserVO vo = userService.getLogonUserInfo(loginId, loginType);
        vo.setPermissionList(loginService.getPermissionList(vo.getUserId()));
        vo.setRoleList(loginService.getRoleList(vo.getUserId()));
        return InnerResult.success(vo);
    }
}
