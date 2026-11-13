package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.def.ConfigApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteConfigVO;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AuthApiHystrix implements AuthApiDef {

    @Override
    public ResultApi<RemoteLogonUserVO> getLogonUserInfo() {
        LogHelp.error(log, "AuthApiHystrix#getLogonUserInfo");
        return ResultApi.hystrixFallBack();
    }
}
