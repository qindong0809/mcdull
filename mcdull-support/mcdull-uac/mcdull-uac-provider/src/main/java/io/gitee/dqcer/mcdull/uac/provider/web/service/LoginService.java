package io.gitee.dqcer.mcdull.uac.provider.web.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.AuthCodeEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.SsoConstant;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ResultParse;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserLoginRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录服务
 *
 * @author dqwcer
 * @since 2022/11/07
 */
@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IUserLoginRepository userLoginRepository;

    @Resource
    private RedissonCache redisClient;

    @Resource
    private CacheChannel cacheChannel;

    @Resource
    private UserService userService;


    /**
     * 验证码 redis key
     */
    private static final String CAPTCHA_CODE_KEY = "captcha_codes:";



    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        this.validateCaptcha(username, code, uuid);
        // 登录前置校验
        this.loginPreCheck(username, password);
        return "";
    }

    private void loginPreCheck(String username, String password) {
        UserDO userEntity = userRepository.get(username);
        if (ObjUtil.isNotNull(userEntity)) {
            StpUtil.login(userEntity.getId());
            return;
        }
        throw new BusinessException(CodeEnum.UN_AUTHORIZATION);
    }

    public void validateCaptcha(String username, String code, String uuid) {

    }

    /**
     * 登录
     *
     * @param loginDTO 登录dto
     * @return {@link Result}<{@link String}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> login(LoginDTO loginDTO) {
        String account = loginDTO.getUsername();
        UserDO userEntity = userRepository.queryUserByAccount(account);
        if (null == userEntity) {
            log.warn("账号不存在 account: {}", account);
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        String password = userEntity.getPassword();

        if (!password.equals(Sha1Util.getSha1(loginDTO.getPassword() + userEntity.getSalt()))) {
            log.warn("用户密码错误");
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        if (!userEntity.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            log.warn("账号已停用 account: {}", account);
            return Result.error(AuthCodeEnum.DISABLE);
        }
//        if (!userEntity.getDelFlag().equals(DelFlayEnum.NORMAL.getCode())) {
//            log.warn("账号已被删除 account: {}", account);
//            return Result.error(AuthCodeEnum.NOT_EXIST);
//        }

        Long userId = userEntity.getId();

        //  强制7天过期
        String token = RandomUtil.uuid();
        CacheUser cacheUser = new CacheUser().setUserId(userId).setLastActiveTime(LocalDateTime.now());
        cacheChannel.put(MessageFormat.format(SsoConstant.SSO_TOKEN, token), cacheUser, SsoConstant.SSO_TOKEN_NAMESPACE_TIMEOUT);

        //  更新登录时间
        userRepository.updateLoginTimeById(userId);

        //  记录登录信息
        userLoginRepository.saveLoginInfoByUserIdAndToken(userId, token);

        StpUtil.login(userId);

        StpUtil.getSession().getLoginId();

        return Result.success(token);
    }

    /**
     * token验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link Long}>
     */
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

        return Result.success(user.getUserId());
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
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
            return Result.success();
        }
        user.setOnlineStatus(CacheUser.LOGOUT);
        redisClient.putIfExists(tokenKey, user);

        //  记录注销信息
        userLoginRepository.saveLogoutInfoByUserIdAndToken(user.getUserId(), token);
        return Result.success();
    }

    public Result<List<String>> getPermissionList(Long userId) {
        Result<List<UserPowerVO>> listResult = userService.queryResourceModules(userId);
        List<UserPowerVO> userPowerVOList = ResultParse.getInstance(listResult);
        Set<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(userPowerVOList)) {
            for (UserPowerVO vo : userPowerVOList) {
                set.addAll(vo.getModules());
            }
        }
        return Result.success(new ArrayList<>(set));
    }


    public Result<List<String>> getRoleList(Long userId) {
        Result<List<UserPowerVO>> listResult = userService.queryResourceModules(userId);
        List<UserPowerVO> userPowerVOList = ResultParse.getInstance(listResult);
        Set<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(userPowerVOList)) {
            set = userPowerVOList.stream().map(UserPowerVO::getCode).collect(Collectors.toSet());
        }
        return Result.success(new ArrayList<>(set));
    }
}
