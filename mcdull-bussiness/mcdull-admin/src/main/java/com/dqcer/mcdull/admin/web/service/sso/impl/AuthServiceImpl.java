package com.dqcer.mcdull.admin.web.service.sso.impl;

import com.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import com.dqcer.mcdull.framework.base.enums.AuthCodeEnum;
import com.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import com.dqcer.mcdull.framework.base.enums.StatusEnum;
import com.dqcer.mcdull.framework.base.exception.BusinessException;
import com.dqcer.mcdull.framework.base.storage.CacheUser;
import com.dqcer.mcdull.framework.base.storage.SsoConstant;
import com.dqcer.mcdull.framework.base.util.ObjUtil;
import com.dqcer.mcdull.framework.base.util.RandomUtil;
import com.dqcer.mcdull.framework.base.util.Sha1Util;
import com.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import com.dqcer.mcdull.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.framework.auth.ISecurityService;
import com.dqcer.mcdull.admin.model.dto.sys.LoginDTO;
import com.dqcer.mcdull.admin.model.entity.sys.UserDO;
import com.dqcer.mcdull.admin.web.dao.repository.sys.IUserLoginRepository;
import com.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import com.dqcer.mcdull.admin.web.service.sso.IAuthService;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.dqcer.mcdull.framework.redis.operation.RedissonCache;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 身份验证服务 业务实现类
 *
 * @author dqcer
 * @date 2023/01/11 22:01:06
 */
@Service
public class AuthServiceImpl implements IAuthService, ISecurityService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IUserLoginRepository userLoginRepository;

    @Resource
    private RedissonCache redisClient;

    @Resource
    private CacheChannel cacheChannel;

    /**
     * 登录
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link String}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        UserDO userEntity = userRepository.queryUserByAccount(account);
        if (null == userEntity) {
            log.warn("账号不存在 account: {}", account);
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        String password = userEntity.getPassword();

        if (!password.equals(Sha1Util.getSha1(loginDTO.getPd() + userEntity.getSalt()))) {
            log.warn("用户密码错误");
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        if (!userEntity.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            log.warn("账号已停用 account: {}", account);
            return Result.error(AuthCodeEnum.DISABLE);
        }
        if (!userEntity.getDelFlag().equals(DelFlayEnum.NORMAL.getCode())) {
            log.warn("账号已被删除 account: {}", account);
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }

        Long userId = userEntity.getId();

        //  强制7天过期
        String token = RandomUtil.uuid();
        CacheUser cacheUser = new CacheUser().setUserId(userId).setLastActiveTime(LocalDateTime.now());
        cacheChannel.put(MessageFormat.format(SsoConstant.SSO_TOKEN, token), cacheUser, SsoConstant.SSO_TOKEN_NAMESPACE_TIMEOUT);

        //  更新登录时间
        userRepository.updateLoginTimeById(userId);

        //  记录登录信息
        userLoginRepository.saveLoginInfoByUserIdAndToken(userId, token);

        return Result.ok(token);
    }

    /**
     * token验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link Long}>
     */
    @Override
    public Result<Long> tokenValid(String token) {
        String tokenKey = MessageFormat.format(SsoConstant.SSO_TOKEN, token);
        CacheUser user = cacheChannel.get(tokenKey, CacheUser.class);

        if (log.isDebugEnabled()) {
            log.debug("CacheUser:{}", user);
        }

        if (ObjUtil.isNull(user)) {
            log.warn("token valid:  7天强制过期下线");
            return Result.error(CodeEnum.UN_AUTHORIZATION);
        }

        if (CacheUser.OFFLINE.equals(user.getOnlineStatus())) {
            log.warn("token valid:  异地登录");
            return Result.error(CodeEnum.OTHER_LOGIN);
        }

        if (CacheUser.LOGOUT.equals(user.getOnlineStatus())) {
            log.warn("token valid:  客户端主动退出");
            return Result.error(CodeEnum.LOGOUT);
        }

        LocalDateTime lastActiveTime = user.getLastActiveTime();

        LocalDateTime now = LocalDateTime.now();
        // TODO: 2022/12/25 过期时间， 需要写入配置文件
        int expirationTime = 30;
        if (lastActiveTime.plusMinutes(expirationTime).isBefore(now)) {
            log.warn("token valid:  用户操作已过期");
            return Result.error(CodeEnum.TIMEOUT_LOGIN);
        }

        // 控制更新频率 lastActiveTime + 2 < now
        int updateFrequency = 2;
        if (lastActiveTime.plusMinutes(updateFrequency).isBefore(now)) {
            redisClient.putIfExists(tokenKey, user.setLastActiveTime(now));
        }

        return Result.ok(user.getUserId());
    }

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link Result}<{@link List}<{@link UserPowerVO}>>
     */
    @Override
    public List<UserPowerVO> queryResourceModules(Long userId) {
        return userRepository.queryResourceModules(userId);
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException();
        }
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(HttpHeaderConstants.TOKEN);
        String tokenKey = MessageFormat.format(SsoConstant.SSO_TOKEN, token);
        CacheUser user = redisClient.get(tokenKey, CacheUser.class);
        if (user == null) {
            log.error("redis 缓存 token过期，这里需要优化处理");
            return Result.ok();
        }
        user.setOnlineStatus(CacheUser.LOGOUT);
        redisClient.putIfExists(tokenKey, user);

        //  记录注销信息
        userLoginRepository.saveLogoutInfoByUserIdAndToken(user.getUserId(), token);
        return Result.ok();
    }
}
