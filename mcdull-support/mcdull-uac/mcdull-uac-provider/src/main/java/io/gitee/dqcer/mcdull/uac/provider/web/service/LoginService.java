package io.gitee.dqcer.mcdull.uac.provider.web.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ResultParse;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserLoginRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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



    public void login(String username, String password, String code, String uuid) {
        // 验证码校验
        this.validateCaptcha(username, code, uuid);
        // 登录前置校验
        Long loginId = this.loginPreCheck(username, password);
        StpUtil.login(loginId);
    }

    private Long loginPreCheck(String username, String passwordDTO) {
        UserDO userEntity = userRepository.get(username);
        if (ObjUtil.isNotNull(userEntity)) {
            String password = userEntity.getPassword();
            if (password.equals(Sha1Util.getSha1(passwordDTO + userEntity.getSalt()))) {
                if (Boolean.FALSE.equals(userEntity.getInactive())) {
                    return userEntity.getId();
                }
                throw new BusinessException("user.account.not.active");
            }
        }
        throw new BusinessException("incorrect.username.or.password");
    }

    public void validateCaptcha(String username, String code, String uuid) {

    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    public void logout() {
        Long userId = UserContextHolder.currentUserId();
        StpUtil.logout(userId);
        log.info("logout. userId : {}", userId);
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
