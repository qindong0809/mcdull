package io.gitee.dqcer.mcdull.blaze.service;

import io.gitee.dqcer.mcdull.blaze.domain.entity.AppUserEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.AppUserFocusTimeEntity;

/**
 * flow-focus 客户端登录与专注数据服务
 *
 * @author dqcer
 */
public interface IFlowFocusService {

    /**
     * 校验账号密码
     *
     * @param loginName 登录名
     * @param password  明文密码
     * @return 登录用户
     */
    AppUserEntity authenticate(String loginName, String password);

    /**
     * 注册账号
     *
     * @param loginName 登录名
     * @param password  明文密码
     * @param email     邮箱
     */
    void register(String loginName, String password, String email);

    /**
     * 保存专注记录
     *
     * @param entity 专注记录
     * @param userId 用户 ID
     */
    void saveFocusData(AppUserFocusTimeEntity entity, Integer userId);
}
