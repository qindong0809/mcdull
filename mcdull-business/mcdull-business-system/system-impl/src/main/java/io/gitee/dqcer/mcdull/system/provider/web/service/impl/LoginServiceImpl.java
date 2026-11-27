package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.system.provider.config.constants.CacheConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.basic.GenericLogic;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.system.facade.model.enums.GenderEnum;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.ApplicationEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LogonVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserPowerVO;
import io.gitee.dqcer.mcdull.system.provider.util.Ip2RegionUtil;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.*;
import jakarta.annotation.Resource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CacheManager cacheManager;
    @Resource
    private IDepartmentService departmentService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private ITenantAuthService tenantAuthService;


    @Override
    public void saveLoginLog(String loginName, LoginLogResultTypeEnum resultTypeEnum, String remark) {
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        loginLogEntity.setLoginName(loginName);
        String ipAddr = IpUtil.getIpAddr(ServletUtil.getRequest());
        loginLogEntity.setLoginIp(ipAddr);
        loginLogEntity.setLoginIpRegion(Ip2RegionUtil.getRegion(ipAddr));
        // login_result 在库中为 NOT NULL 且无默认值，必须保证有值
        if (ObjUtil.isNull(resultTypeEnum)) {
            resultTypeEnum = LoginLogResultTypeEnum.LOGIN_FAIL;
        }
        loginLogEntity.setLoginResult(resultTypeEnum.getCode());
        loginLogEntity.setUserAgent(ServletUtil.getUserAgent());
        loginLogEntity.setRemark(remark);
        try {
            loginLogService.add(loginLogEntity);
        } catch (Exception e) {
            // 登录日志是旁路记录，写失败不能让登录主流程回滚：
            // ProcessFlow.run() 带 @Transactional，而 Sa-Token 登录写 Redis 不受回滚影响，
            // 抛异常会导致「Redis 已登录但接口返回失败」，反复重试把会话顶下线。
            LogHelp.error(log, "saveLoginLog error, loginName: {}", loginName, e);
        }
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
        vo.setGenderName(GenderEnum.textOf(userEntity.getGender()));
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
        List<MenuVO> list = menuService.getList(userEntity.getId(), userEntity.getAdministratorFlag());
        // 排序
        list.sort((o1, o2) -> NumberUtil.compare(Convert.toInt(o1.getSort(),0), Convert.toInt(o2.getSort(), 0)));
        list.sort(Comparator.comparing(MenuVO::getMenuId));
        vo.setMenuList(list);
        vo.setTenantList(this.buildTenantList(userEntity));
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
        Set<String> permissionCodeSet = new HashSet<>();
        Map<Integer, UserEntity> entityMap = userService.getEntityMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(entityMap)) {
            UserEntity userDO = entityMap.get(userId);
            if (ObjUtil.isNotNull(userDO)) {
                Boolean administratorFlag = userDO.getAdministratorFlag();
                if (BooleanUtil.isFalse(administratorFlag)) {
                    List<UserPowerVO> userPowerVOList = userService.getResourceModuleList(userId);
                    if (CollUtil.isNotEmpty(userPowerVOList)) {
                        for (UserPowerVO vo : userPowerVOList) {
                            permissionCodeSet.addAll(vo.getModules());
                        }
                    }
                }
            }
        }
        return new ArrayList<>(permissionCodeSet);
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
    public LogonVO getCurrentUserInfo(String code) {
        Integer userId = UserContextHolder.userId();
        String appCode = StrUtil.blankToDefault(code, "all");
        Integer tenantId = this.currentTenantId();
        String key = StrUtil.format("current:user:{}:app:{}:tenant:{}:v2:role:list", userId, appCode,
                tenantId == null ? 0 : tenantId);
        Cache cache = cacheManager.getCache(CacheConstants.BUSINESS_CACHE);
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
            vo.setCurrentAppCode(appCode);
            vo.setMenuList(this.filterMenuByTenant(this.filterMenuByApp(vo.getMenuList(), code), tenantId, userEntity));
            if (ObjUtil.isNotNull(cache)) {
                cache.put(key, vo);
            }
            return vo;
        }
        LogHelp.error(log, "getCurrentUserInfo error, userId: {}, appCode: {}", userId, appCode);
        return null;
    }

    private Integer currentTenantId() {
        if (ServletUtil.getRequest() == null) {
            return null;
        }
        String value = ServletUtil.getRequest().getHeader(HttpHeaderConstants.T_ID);
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new BusinessException("system.tenant.required");
        }
    }

    private List<MenuVO> filterMenuByTenant(List<MenuVO> menuList, Integer tenantId, UserEntity userEntity) {
        if (tenantId == null || CollUtil.isEmpty(menuList)) {
            return menuList;
        }
        Set<Integer> accessible = BooleanUtil.isTrue(userEntity.getAdministratorFlag())
                ? tenantAuthService.allActiveTenantIds()
                : tenantAuthService.authorizedTenantIds(userEntity.getId());
        if (!accessible.contains(tenantId)) {
            throw new BusinessException("system.tenant.forbidden");
        }

        List<String> applicationCodes = tenantAuthService.applicationCodes(tenantId);
        Set<Integer> whitelist = tenantAuthService.menuIds(tenantId);
        Map<Integer, MenuVO> menuMap = menuList.stream()
                .filter(menu -> ObjUtil.isNotNull(menu.getMenuId()))
                .collect(Collectors.toMap(MenuVO::getMenuId, menu -> menu, (a, b) -> a));
        Set<Integer> keepIds = new HashSet<>();
        for (MenuVO menu : menuList) {
            String applicationCode = StrUtil.blankToDefault(menu.getApplicationCode(), "system");
            boolean platformMenu = "system".equals(applicationCode);
            boolean tenantMenu = applicationCodes.contains(applicationCode);
            // 白名单为空表示租户未配置任何菜单，不得放行为全部租户菜单；平台菜单仍按平台权限放行
            boolean allowed = !whitelist.isEmpty() && whitelist.contains(menu.getMenuId());
            if (platformMenu || (tenantMenu && allowed)) {
                keepIds.add(menu.getMenuId());
                Integer parentId = menu.getParentId();
                while (ObjUtil.isNotNull(parentId) && menuMap.containsKey(parentId)) {
                    keepIds.add(parentId);
                    parentId = menuMap.get(parentId).getParentId();
                }
            }
        }
        if (keepIds.isEmpty()) {
            return Collections.emptyList();
        }
        return menuList.stream().filter(menu -> keepIds.contains(menu.getMenuId())).collect(Collectors.toList());
    }

    private List<TenantVO> buildTenantList(UserEntity userEntity) {
        Set<Integer> tenantIds = BooleanUtil.isTrue(userEntity.getAdministratorFlag())
                ? tenantAuthService.allActiveTenantIds()
                : tenantAuthService.authorizedTenantIds(userEntity.getId());
        return tenantAuthService.tenantList(tenantIds);
    }

    private List<MenuVO> filterMenuByApp(List<MenuVO> menuList, String appCode) {
        if (StrUtil.isBlank(appCode) || CollUtil.isEmpty(menuList)) {
            return menuList;
        }
        ApplicationEnum application = Arrays.stream(ApplicationEnum.values())
                .filter(item -> item.getCode().equals(appCode))
                .findFirst()
                .orElse(null);
        if (ObjUtil.isNull(application)) {
            return menuList;
        }
        String prefix = application.getPermissionPrefix();
        String pathPrefix = application.getPathPrefix();
        Map<Integer, MenuVO> menuMap = menuList.stream()
                .filter(menu -> ObjUtil.isNotNull(menu.getMenuId()))
                .collect(Collectors.toMap(MenuVO::getMenuId, menu -> menu, (a, b) -> a));
        Set<Integer> keepIds = new HashSet<>();
        for (MenuVO menu : menuList) {
            String webPerms = StrUtil.blankToDefault(menu.getWebPerms(), "");
            String apiPerms = StrUtil.blankToDefault(menu.getApiPerms(), "");
            String path = StrUtil.blankToDefault(menu.getPath(), "");
            boolean matched = StrUtil.startWith(webPerms, prefix)
                    || StrUtil.startWith(apiPerms, prefix)
                    || (StrUtil.isNotBlank(pathPrefix) && StrUtil.contains(path, pathPrefix));
            if (matched && ObjUtil.isNotNull(menu.getMenuId())) {
                keepIds.add(menu.getMenuId());
                Integer parentId = menu.getParentId();
                while (ObjUtil.isNotNull(parentId) && menuMap.containsKey(parentId)) {
                    keepIds.add(parentId);
                    parentId = menuMap.get(parentId).getParentId();
                }
            }
        }
        if (keepIds.isEmpty()) {
            return Collections.emptyList();
        }
        return menuList.stream().filter(menu -> keepIds.contains(menu.getMenuId())).collect(Collectors.toList());
    }

    @Override
    public Boolean isCaptchaEnabled() {
        return commonManager.isCaptchaEnabled();
    }
}
