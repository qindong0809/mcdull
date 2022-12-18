package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.uac.provider.model.entity.UserLoginDO;

public interface IUserLoginRepository extends IService<UserLoginDO> {

    /**
     * 保存登录信息根据用户id和令牌
     *
     * @param userId 用户id
     * @param token  令牌
     */
    void saveLoginInfoByUserIdAndToken(Long userId, String token);

    /**
     * 保存注销信息根据用户id和令牌
     *
     * @param userId 用户id
     * @param token  令牌
     */
    void saveLogoutInfoByUserIdAndToken(Long userId, String token);
}
