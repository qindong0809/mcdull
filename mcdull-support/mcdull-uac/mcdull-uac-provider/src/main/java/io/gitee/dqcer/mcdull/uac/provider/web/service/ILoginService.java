package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;

/**
 * @author dqcer
 */
public interface ILoginService {

    /**
     * 登录
     *
     * @param username username
     * @param password password
     * @param code     code
     * @param uuid     uuid
     */
    void login(String username, String password, String code, String uuid);

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
    List<String> getPermissionList(Long userId);

    /**
     * 获取角色列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getRoleList(Long userId);
}
