package io.gitee.dqcer.mcdull.focus.web.service;

import io.gitee.dqcer.mcdull.focus.model.entity.AppUserEntity;

/**
 * 应用用户服务
 *
 * @author dqcer
 */
public interface IAppUserService {

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
     * 按用户 ID 查询
     *
     * @param userId 用户 ID
     * @return 用户
     */
    AppUserEntity getByUserId(Integer userId);

    /**
     * 更新主题
     *
     * @param userId 用户 ID
     * @param theme  主题
     */
    void updateTheme(Integer userId, Integer theme);

    /**
     * 更新个人资料
     *
     * @param userId 用户 ID
     * @param gender 性别
     * @param stage  阶段
     */
    void updateProfile(Integer userId, String gender, String stage);

    /**
     * 开通会员
     *
     * @param userId 用户 ID
     */
    void openVip(Integer userId);

    /**
     * 注销账号
     *
     * @param userId 用户 ID
     */
    void deleteById(Integer userId);
}
