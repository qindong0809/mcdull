package io.gitee.dqcer.mcdull.gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import io.gitee.dqcer.mcdull.framework.feign.ResultApiParse;
import io.gitee.dqcer.mcdull.system.facade.service.AuthApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * stp接口实现类
 * @author dqcer
 * @since 2026/01/15
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private AuthApi authApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return this.getLogonUserInfo().getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return this.getLogonUserInfo().getRoleList();
    }

    private RemoteLogonUserVO getLogonUserInfo() {
        return ResultApiParse.getInstance(authApi.getLogonUserInfo());
    }
}
