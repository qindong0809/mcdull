package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginDeviceEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.util.Ip2RegionUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private IUserService userService;

    @Resource
    private IMenuService menuService;

    @Resource
    private ICaptchaService captchaService;

    @Resource
    private ILoginLogService loginLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LogonVO login(LoginDTO dto) {
        String message = null;
        LoginLogResultTypeEnum typeEnum = LoginLogResultTypeEnum.LOGIN_SUCCESS;
        try {
            this.validateCaptcha(dto.getCaptchaCode(), dto.getCaptchaUuid());
            // 登录前置校验
            UserEntity userEntity = this.loginPreCheck(dto.getLoginName(), dto.getPassword());
            StpUtil.login(userEntity.getId(), IEnum.getByCode(LoginDeviceEnum.class, dto.getLoginDevice()).getText());
            StpUtil.getSession().set(GlobalConstant.ADMINISTRATOR_FLAG, userEntity.getAdministratorFlag());
            return this.buildLogonVo(userEntity);
        } catch (Exception e) {
            message = e.getMessage();
            typeEnum = LoginLogResultTypeEnum.LOGIN_FAIL;
            throw e;
        } finally {
            this.saveLoginLog(dto.getLoginName(), typeEnum, message);
        }
    }

    private void saveLoginLog(String loginName, LoginLogResultTypeEnum resultTypeEnum, String remark) {
        LoginLogEntity log = new LoginLogEntity();
        log.setLoginName(loginName);
        String ipAddr = IpUtil.getIpAddr(ServletUtil.getRequest());
        log.setLoginIp(ipAddr);
        log.setLoginIpRegion(Ip2RegionUtil.getRegion(ipAddr));
        log.setLoginResult(resultTypeEnum.getCode());
        log.setUserAgent(getUserAgent());
        log.setRemark(remark);
        loginLogService.add(log);
    }

    private static String getUserAgent() {
        HttpServletRequest request = ServletUtil.getRequest();
        String userAgent = null;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            if ("User-Agent".equalsIgnoreCase(element)) {
                userAgent = request.getHeader(element);
            }
        }
        return userAgent;
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logout() {
        Integer userId = UserContextHolder.userId();
        UserEntity user = userService.get(userId);
        StpUtil.logout(userId);
        if (ObjUtil.isNotNull(user)) {
            this.saveLoginLog(user.getLoginName(), LoginLogResultTypeEnum.LOGIN_OUT, StrUtil.EMPTY);
        }
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

    @Override
    public LogonVO getCurrentUserInfo() {
        Integer userId = UserContextHolder.userId();
        UserEntity userEntity = userService.get(userId);
        if (ObjUtil.isNotNull(userEntity)) {
            LogonVO vo = this.buildLogonVo(userEntity);
            vo.setToken(StpUtil.getTokenValue());
            return vo;
        }
        return null;
    }
}
