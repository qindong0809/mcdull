package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserLoginEntity;

/**
 * 用户登录信息 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserLoginRepository extends IService<UserLoginEntity> {

    /**
     * 保存登录信息根据用户id和令牌
     *
     * @param userId 用户id
     * @param token  令牌
     */
    void saveLoginInfoByUserIdAndToken(Integer userId, String token);

    /**
     * 保存注销信息根据用户id和令牌
     *
     * @param userId 用户id
     * @param token  令牌
     */
    void saveLogoutInfoByUserIdAndToken(Integer userId, String token);
}
