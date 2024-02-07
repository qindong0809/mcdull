package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;

/**
 * @author dqcer
 */
public interface ILoginService {

    void login(String username, String password, String code, String uuid);

    void logout();

    List<String> getPermissionList(Long userId);

    List<String> getRoleList(Long userId);
}
