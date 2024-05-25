package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;

import java.util.List;

/**
 * @author dqcer
 */
public interface ILoginService {

    /**
     * 登录
     *
     * @param dto dto
     * @return {@link LogonVO}
     */
    LogonVO login(LoginDTO dto);

    /**
     * 注销
     */
    void logout();

    /**
     * 获取权限列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getPermissionList(Integer userId);

    /**
     * 获取角色列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getRoleList(Integer userId);

    /**
     * 获取当前用户信息
     *
     * @return {@link LogonVO}
     */
    LogonVO getCurrentUserInfo();
}
