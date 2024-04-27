package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


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
    private RedissonCache redissonCache;

    @Resource
    private IUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LogonVO login(String username, String password, String code, String uuid) {
        // todo 验证码校验
        this.validateCaptcha(username, code, uuid);
        // 登录前置校验
        Long loginId = this.loginPreCheck(username, password);
        StpUtil.login(loginId);
        return this.buildLogonVo();
    }

    private LogonVO buildLogonVo() {
        return new LogonVO();
    }

    private void validateCaptcha(String username, String code, String uuid) {

    }

    private Long loginPreCheck(String username, String passwordDTO) {
        UserEntity userEntity = userService.get(username);
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


    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @Override
    public void logout() {
        StpUtil.logout(UserContextHolder.currentUserId());
    }

    @Override
    public List<String> getPermissionList(Integer userId) {
        Map<Integer, UserEntity> entityMap = userService.getEntityMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(entityMap)) {
            UserEntity userDO = entityMap.get(userId);
            if (ObjUtil.isNotNull(userDO)) {
                Boolean administratorFlag = userDO.getAdministratorFlag();
                if (BooleanUtil.isTrue(administratorFlag)) {
                    return ListUtil.of("*:*:*");
                }
            }
        }
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
    public List<String> getRoleList(Integer userId) {
        List<UserPowerVO> userPowerVOList = userService.getResourceModuleList(userId);
        Set<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(userPowerVOList)) {
            set = userPowerVOList.stream().map(UserPowerVO::getCode).collect(Collectors.toSet());
        }
        return new ArrayList<>(set);
    }
}
