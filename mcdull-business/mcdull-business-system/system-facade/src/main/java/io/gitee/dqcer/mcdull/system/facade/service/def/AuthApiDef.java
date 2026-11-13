package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Config Api Def
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface AuthApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM +"/auth/logon-user")
    ResultApi<RemoteLogonUserVO> getLogonUserInfo();
}
