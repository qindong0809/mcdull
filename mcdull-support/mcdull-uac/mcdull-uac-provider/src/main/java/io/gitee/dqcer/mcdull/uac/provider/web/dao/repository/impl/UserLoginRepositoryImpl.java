package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserLoginEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.UserLoginMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserLoginRepository;
import org.springframework.stereotype.Service;

/**
 * 用户登录信息 数据库操作实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserLoginRepositoryImpl extends ServiceImpl<UserLoginMapper, UserLoginEntity> implements IUserLoginRepository {

    /**
     * 登录
     */
    public static final Integer LOGIN = 1;

    /**
     * 注销
     */
    public static final Integer LOGOUT = 2;

    /**
     * 保存登录信息根据用户id和令牌
     *
     * @param userId 用户id
     * @param token  令牌
     */
    @Override
    public void saveLoginInfoByUserIdAndToken(Integer userId, String token) {
        save(userId, token, LOGIN);
    }

    /**
     * 保存注销信息根据用户id和令牌
     *
     * @param userId 用户id
     * @param token  令牌
     */
    @Override
    public void saveLogoutInfoByUserIdAndToken(Integer userId, String token) {
        save(userId, token, LOGOUT);
    }

    /**
     * 保存
     *
     * @param userId 用户id
     * @param token  令牌
     * @param type   类型 1/登录 2/注销
     */
    private void save(Integer userId, String token, Integer type) {
        UserLoginEntity loginDO = new UserLoginEntity();
//        loginDO.setType(type);
//        loginDO.setToken(token);
//        loginDO.setUserId(userId);
//        int rowSize = baseMapper.insert(loginDO);
//        if (rowSize == GlobalConstant.Database.ROW_0) {
//            throw new DatabaseRowException(CodeEnum.DB_ERROR);
//        }
    }
}
