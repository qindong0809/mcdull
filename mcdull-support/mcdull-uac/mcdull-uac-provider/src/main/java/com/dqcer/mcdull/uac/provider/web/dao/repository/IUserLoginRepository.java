package io.gitee.dqcer.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.uac.provider.model.entity.UserLoginDO;

/**
 * 用户登录信息 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/26
 */
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
