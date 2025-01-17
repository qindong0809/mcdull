package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.uac.provider.util.Ip2RegionUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Login Service
 *
 * @author dqwcer
 * @since 2022/11/07
 */
@Service
public class LoginServiceImpl extends GenericLogic implements ILoginService {

    @Resource
    private IUserService userService;

    @Resource
    private IMenuService menuService;

    @Resource
    private ILoginLogService loginLogService;

    @Resource
    private CaffeineCacheManager cacheManager;

    @Resource
    private IDepartmentService departmentService;


    @Override
    public void saveLoginLog(String loginName, LoginLogResultTypeEnum resultTypeEnum, String remark) {
        LoginLogEntity log = new LoginLogEntity();
        log.setLoginName(loginName);
        String ipAddr = IpUtil.getIpAddr(ServletUtil.getRequest());
        log.setLoginIp(ipAddr);
        log.setLoginIpRegion(Ip2RegionUtil.getRegion(ipAddr));
        log.setLoginResult(resultTypeEnum.getCode());
        log.setUserAgent(ServletUtil.getUserAgent());
        log.setRemark(remark);
        loginLogService.add(log);
    }

    @Override
    public LogonVO buildLogonVo(UserEntity userEntity) {
        LogonVO vo = new LogonVO();
        vo.setToken(StpUtil.getTokenValue());
        vo.setEmployeeId(userEntity.getId());
        vo.setLoginName(userEntity.getLoginName());
        vo.setActualName(userEntity.getActualName());
        vo.setDepartmentId(userEntity.getDepartmentId());
        vo.setAdministratorFlag(userEntity.getAdministratorFlag());
        vo.setGender(userEntity.getGender());
        vo.setPhone(userEntity.getPhone());
        vo.setIp(IpUtil.getIpAddr(ServletUtil.getRequest()));
        vo.setUserAgent(ServletUtil.getUserAgent());
        Integer departmentId = userEntity.getDepartmentId();
        if (ObjUtil.isNotNull(departmentId)) {
            DepartmentEntity department = departmentService.getById(departmentId);
            if (ObjUtil.isNotNull(department)) {
                vo.setDepartmentName(department.getName());
            }
        }
        vo.setMenuList(menuService.getList(userEntity.getId(), userEntity.getAdministratorFlag()));
        LoginLogEntity lastLoginLog = loginLogService.getLastLoginLog(userEntity.getLoginName());
        if (ObjUtil.isNotNull(lastLoginLog)) {
            vo.setLastLoginIp(lastLoginLog.getLoginIp());
            vo.setLastLoginIpRegion(lastLoginLog.getLoginIpRegion());
            vo.setLastLoginUserAgent(lastLoginLog.getUserAgent());
            vo.setLastLoginTime(lastLoginLog.getCreatedTime());
        }
       return vo;
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


    @Cacheable(cacheNames = "getRoleList", key = "#userId")
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
        String key = StrUtil.format("current:user:{}:role:list", userId);
        Cache cache = cacheManager.getCache(GlobalConstant.CAFFEINE_CACHE);
        if (ObjUtil.isNotNull(cache)) {
            LogonVO vo = cache.get(key, LogonVO.class);
            if (ObjUtil.isNotNull(vo)) {
                return vo;
            }
        }
        UserEntity userEntity = userService.get(userId);
        if (ObjUtil.isNotNull(userEntity)) {
            LogonVO vo = this.buildLogonVo(userEntity);
            vo.setToken(StpUtil.getTokenValue());
            if (ObjUtil.isNotNull(cache)) {
                cache.put(key, vo);
            }
            return vo;
        }
        LogHelp.error(log, "getCurrentUserInfo error, userId: {}", userId);
        return null;
    }
}
