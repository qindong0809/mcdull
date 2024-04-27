package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginDeviceEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
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

    @Resource
    private IRoleService roleService;

    @Resource
    private IMenuService menuService;

    @Resource
    private ICaptchaService captchaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LogonVO login(LoginDTO dto) {
        this.validateCaptcha(dto.getCaptchaCode(), dto.getCaptchaUuid());
        // 登录前置校验
        UserEntity userEntity = this.loginPreCheck(dto.getLoginName(), dto.getPassword());
        StpUtil.login(userEntity.getId(), IEnum.getByCode(LoginDeviceEnum.class, dto.getLoginDevice()).getText());
        StpUtil.getSession().set("administratorFlag", userEntity.getAdministratorFlag());
        return this.buildLogonVo(userEntity);
    }

    private LogonVO buildLogonVo(UserEntity userEntity) {
        LogonVO logonVO = new LogonVO();
        logonVO.setToken(StpUtil.getTokenValue());
        logonVO.setEmployeeId(userEntity.getId());
        logonVO.setLoginName(userEntity.getLoginName());
//        logonVO.setUserType(userEntity.getUserType());
        logonVO.setActualName(userEntity.getActualName());
        logonVO.setDepartmentId(userEntity.getDepartmentId());
//        logonVO.setDepartmentName(userEntity.getDepartmentName());
        logonVO.setAdministratorFlag(userEntity.getAdministratorFlag());
        logonVO.setGender(userEntity.getGender());
        logonVO.setPhone(userEntity.getPhone());
//        logonVO.setIp(UserContextHolder.currentIp());
//        logonVO.setUserAgent(UserContextHolder.currentUserAgent());
//        logonVO.setLastLoginIp(userEntity.getLastLoginIp());
        logonVO.setMenuList(menuService.getList(userEntity.getId(), userEntity.getAdministratorFlag()));
       return logonVO;
    }



    private void validateCaptcha(String code, String uuid) {
        captchaService.checkCaptcha(code, uuid);
    }

    private UserEntity loginPreCheck(String username, String passwordDTO) {
        UserEntity userEntity = userService.get(username);
        if (ObjUtil.isNotNull(userEntity)) {
            boolean isOk = userService.passwordCheck(userEntity, passwordDTO);
            if (isOk) {
                if (Boolean.TRUE.equals(userEntity.getInactive())) {
                    throw new BusinessException("user.account.not.active");
                }
                return userEntity;
            }
        }
        throw new BusinessException("incorrect.username.or.password");
    }



    @Override
    public void logout() {
        StpUtil.logout(UserContextHolder.currentUserId());
    }

    @Override
    public List<String> getPermissionList(Long userId) {
        Map<Long, UserEntity> entityMap = userService.getEntityMap(ListUtil.of(userId));
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
    public List<String> getRoleList(Long userId) {
        List<UserPowerVO> userPowerVOList = userService.getResourceModuleList(userId);
        Set<String> set = new HashSet<>();
        if (CollUtil.isNotEmpty(userPowerVOList)) {
            set = userPowerVOList.stream().map(UserPowerVO::getCode).collect(Collectors.toSet());
        }
        return new ArrayList<>(set);
    }

    @Override
    public LogonVO getCurrentUserInfo(Long userId) {
        if (ObjUtil.isNotNull(userId)) {
            UserEntity userEntity = userService.get(userId);
            if (ObjUtil.isNotNull(userEntity)) {
                return this.buildLogonVo(userEntity);
            }
        }
        return null;
    }
}
