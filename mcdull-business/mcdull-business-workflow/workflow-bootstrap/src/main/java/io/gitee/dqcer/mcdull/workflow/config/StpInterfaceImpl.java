package io.gitee.dqcer.mcdull.workflow.config;

import cn.dev33.satoken.stp.StpInterface;
import io.gitee.dqcer.mcdull.system.facade.service.AuthAPi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 工作流服务 sa-token 权限接口实现，权限与角色由 system 服务远程提供。
 *
 * @author dqcer
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private AuthAPi authApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return this.getLogonUserInfo(loginId, loginType).getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return this.getLogonUserInfo(loginId, loginType).getRoleList();
    }

    private RemoteLogonUserVO getLogonUserInfo(Object loginId, String loginType) {
        return authApi.getLogonUserInfo(loginId, loginType).unwrap();
    }
}
