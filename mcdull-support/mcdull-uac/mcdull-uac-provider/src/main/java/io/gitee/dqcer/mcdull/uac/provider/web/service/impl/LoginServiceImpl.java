package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.gitee.dqcer.mcdull.uac.provider.web.controller.CaptchaController.CAPTCHA;

/**
 * 登录服务
 *
 * @author dqwcer
 * @since 2022/11/07
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private RedissonCache redissonCache;

    @Resource
    private IUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void login(String username, String password, String code, String uuid) {
        // todo 验证码校验
//        this.validateCaptcha(username, code, uuid);
        // 登录前置校验
        Long loginId = this.loginPreCheck(username, password);
        StpUtil.login(loginId);
        userService.updateLoginTime(loginId, UserContextHolder.getSession().getNow());
    }

    private Long loginPreCheck(String username, String passwordDTO) {
        UserDO userEntity = userRepository.get(username);
        if (ObjUtil.isNotNull(userEntity)) {
            boolean isOk = userService.passwordCheck(userEntity, passwordDTO);
            if (isOk) {
                if (Boolean.TRUE.equals(userEntity.getInactive())) {
                    throw new BusinessException("user.account.not.active");
                }
                return userEntity.getId();
            }
        }
        throw new BusinessException("incorrect.username.or.password");
    }

    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CAPTCHA + uuid;
        String captcha = redissonCache.get(verifyKey, String.class);
        if (StrUtil.isEmpty(captcha)) {
            throw new BusinessException("user.captcha.expire");
        }
        if (!StrUtil.equals(captcha, code)) {
            throw new BusinessException("user.captcha.error");
        }
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @Override
    public void logout() {
        Long userId = UserContextHolder.currentUserId();
        StpUtil.logout(userId);
        log.info("logout. userId : {}", userId);
    }

    @Override
    public List<String> getPermissionList(Long userId) {
        List<UserPowerVO> userPowerVOList = userService.getResourceModuleList(userId);
        Set<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(userPowerVOList)) {
            for (UserPowerVO vo : userPowerVOList) {
                set.addAll(vo.getModules());
            }
        }
        return new ArrayList<>(set);
    }


    @Override
    public List<String> getRoleList(Long userId) {
        List<UserPowerVO> userPowerVOList = userService.getResourceModuleList(userId);
        Set<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(userPowerVOList)) {
            set = userPowerVOList.stream().map(UserPowerVO::getCode).collect(Collectors.toSet());
        }
        return new ArrayList<>(set);
    }
}
