package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;

import java.util.List;

/**
 * Login service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
 */

public interface ILoginService {

    void saveLoginLog(String loginName, LoginLogResultTypeEnum resultTypeEnum, String remark);

    LogonVO buildLogonVo(UserEntity userEntity);

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
