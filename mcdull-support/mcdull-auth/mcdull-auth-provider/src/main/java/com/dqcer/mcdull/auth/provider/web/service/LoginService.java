package com.dqcer.mcdull.auth.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dqcer.framework.base.auth.CacheUser;
import com.dqcer.framework.base.constants.CacheConstant;
import com.dqcer.framework.base.entity.SuperId;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.utils.Sha1Util;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.auth.api.dto.LoginDTO;
import com.dqcer.mcdull.auth.api.entity.SysUserEntity;
import com.dqcer.mcdull.auth.provider.constants.AuthCode;
import com.dqcer.mcdull.auth.provider.web.dao.UserDAO;
import com.dqcer.mcdull.framework.redis.operation.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务
 *
 * @author dqwcer
 * @date 2022/11/07
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private UserDAO userDAO;

    @Resource
    private RedisClient redisClient;

    public Result<String> login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        LambdaQueryWrapper<SysUserEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(SysUserEntity::getAccount, account);
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
        if (!entity.getDefFlay().equals(DelFlayEnum.NORMAL.getCode())) {
            log.warn("账号已被删除 account: {}", account);
            return Result.error(AuthCode.NOT_EXIST);
        }
        String token = UUID.randomUUID().toString();

        CacheUser cacheUser = new CacheUser().setAccountId(entity.getId()).setLastActiveTime(LocalDateTime.now());
        //  强制7天过期
        redisClient.set(MessageFormat.format(CacheConstant.SSO_TOKEN, token), cacheUser, CacheConstant.SSO_TOKEN_NAMESPACE_TIMEOUT, TimeUnit.MILLISECONDS);

        //  更新登录时间
        LambdaUpdateWrapper<SysUserEntity> update = Wrappers.lambdaUpdate();
        update.set(SysUserEntity::getLastLoginTime, new Date());
        update.eq(SuperId::getId, entity.getId());
        userDAO.update(null, update);

        log.info("账号: {} 用户名: {} 已登录", entity.getAccount(), entity.getEmail());

        return Result.ok(token);
    }
}
