package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthApiHystrix implements AuthApiDef {

    @Override
    public InnerResult<RemoteLogonUserVO> getLogonUserInfo(Object loginId, String loginType) {
        LogHelp.error(log, "AuthApiHystrix#getLogonUserInfo");
        return InnerResult.hystrixFallBack();
    }
}
