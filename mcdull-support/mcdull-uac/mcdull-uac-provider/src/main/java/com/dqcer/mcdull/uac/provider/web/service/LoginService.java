package com.dqcer.mcdull.uac.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dqcer.framework.base.auth.CacheUser;
import com.dqcer.framework.base.auth.UserContextHolder;
import com.dqcer.framework.base.constants.CacheConstant;
import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.entity.SuperId;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.utils.ObjUtil;
import com.dqcer.framework.base.utils.Sha1Util;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.framework.redis.operation.CaffeineCache;
import com.dqcer.mcdull.framework.redis.operation.RedisClient;
import com.dqcer.mcdull.uac.api.dto.LoginDTO;
import com.dqcer.mcdull.uac.api.entity.SysUserEntity;
import com.dqcer.mcdull.uac.provider.config.constants.AuthCode;
import com.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务
 *
 * @author dqwcer
 * @version 2022/11/07
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private UserMapper userDAO;

    @Resource
    private RedisClient redisClient;
    
    @Resource
    private CaffeineCache caffeineCache;

    /**
     * 登录
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link String}>
     */
    public Result<String> login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        LambdaQueryWrapper<SysUserEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUserEntity::getAccount, account);
        SysUserEntity entity = userDAO.selectOne(wrapper);
        if (null == entity) {
            log.warn("账号不存在 account: {}", account);
            return Result.error(AuthCode.NOT_EXIST);
        }
        String password = entity.getPassword();

        if (!password.equals(Sha1Util.getSha1(loginDTO.getPd() + entity.getSalt()))) {
            log.warn("用户密码错误");
            return Result.error(AuthCode.NOT_EXIST);
        }
        if (!entity.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            log.warn("账号已停用 account: {}", account);
            return Result.error(AuthCode.DISABLE);
        }
        if (!entity.getDelFlag().equals(DelFlayEnum.NORMAL.getCode())) {
            log.warn("账号已被删除 account: {}", account);
            return Result.error(AuthCode.NOT_EXIST);
        }
        String token = UUID.randomUUID().toString();

        CacheUser cacheUser = new CacheUser().setUserId(entity.getId()).setLastActiveTime(LocalDateTime.now());
        //  强制7天过期
        redisClient.set(MessageFormat.format(CacheConstant.SSO_TOKEN, token), cacheUser, CacheConstant.SSO_TOKEN_NAMESPACE_TIMEOUT, TimeUnit.SECONDS);

        //  更新登录时间
        LambdaUpdateWrapper<SysUserEntity> update = Wrappers.lambdaUpdate();
        update.set(SysUserEntity::getLastLoginTime, new Date());
        update.eq(SuperId::getId, entity.getId());
        userDAO.update(null, update);

        log.info("账号: {} 用户名: {} 已登录", entity.getAccount(), entity.getEmail());

        return Result.ok(token);
    }

    /**
     * token验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link Long}>
     */
    public Result<Long> tokenValid(String token) {
        //  瞬态，本地缓存取
        String tokenKey = MessageFormat.format(CacheConstant.SSO_TOKEN, token);
        CacheUser cacheUser = caffeineCache.get(tokenKey, CacheUser.class);
        if (null != cacheUser) {
            return Result.ok(cacheUser.getUserId());
        }

        Object obj = redisClient.get(tokenKey);

        if (ObjUtil.isNull(obj)) {
            log.warn("token valid:  7天强制过期下线");
            return Result.error(ResultCode.UN_AUTHORIZATION);
        }

        CacheUser user = (CacheUser) obj;
        if (log.isDebugEnabled()) {
            log.debug("CacheUser:{}", user);
        }

        if (CacheUser.OFFLINE.equals(user.getOnlineStatus())) {
            log.warn("token valid:  异地登录");
            return Result.error(ResultCode.OTHER_LOGIN);
        }

        if (CacheUser.LOGOUT.equals(user.getOnlineStatus())) {
            log.warn("token valid:  客户端主动退出");
            return Result.error(ResultCode.LOGOUT);
        }

        LocalDateTime lastActiveTime = user.getLastActiveTime();

        LocalDateTime now = LocalDateTime.now();
        if (lastActiveTime.plusMinutes(30).isBefore(now)) {
            log.warn("token valid:  用户操作已过期");
            return Result.error(ResultCode.TIMEOUT_LOGIN);
        }
        redisClient.set(tokenKey, user.setLastActiveTime(now));

        // FIXME: 2022/11/7 这个过期时间没有用到默认3s过期
        caffeineCache.put(tokenKey, user, 0);
        return Result.ok(user.getUserId());
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    public Result<String> logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(HttpHeaderConstants.TOKEN);
        String tokenKey = MessageFormat.format(CacheConstant.SSO_TOKEN, token);
        //  移除本地缓存
        caffeineCache.evict(tokenKey);
        Object obj = redisClient.get(tokenKey);
        if (obj == null) {
            log.error("redis 缓存 token过期，这里需要优化处理");
            return Result.ok();
        }
        CacheUser user = (CacheUser) obj;

        user.setOnlineStatus(CacheUser.LOGOUT);
        redisClient.set(tokenKey, user);

        log.info("用户:  {} 已主动退出", UserContextHolder.getSession().getUserId());
        return Result.ok();
    }
}
