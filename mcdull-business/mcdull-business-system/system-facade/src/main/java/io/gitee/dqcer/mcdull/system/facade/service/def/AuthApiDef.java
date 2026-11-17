package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.AuthApiHystrix;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Config Api Def
 *
 * @author dqcer
 * @since 2022/10/28
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "AuthApi", fallback = AuthApiHystrix.class)
public interface AuthApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM +"/auth/logon-user")
    InnerResult<RemoteLogonUserVO> getLogonUserInfo(Object loginId, String loginType);
}
