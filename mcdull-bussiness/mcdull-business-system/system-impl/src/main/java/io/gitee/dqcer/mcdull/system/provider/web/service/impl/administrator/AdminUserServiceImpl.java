package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUserSession;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.security.PasswordUtil;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminDeptEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminMenuVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.PermissionBO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.MenuInfoBO;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminDeptService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminUserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICaptchaService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.*;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserEntity> implements IAdminUserService {

    @Resource
    private ICaptchaService captchaService;
    @Resource
    private IAdminMenuService adminMenuService;
    @Resource
    private IAdminUserMenuService adminUserMenuService;
    @Resource
    private IAdminDeptService adminDeptService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String auth(@Valid LogonDTO dto) {
        // todo
//        this.validateCaptcha(dto.getCaptcha(), dto.getUuid());
        AdminUserEntity adminUser = this.getUserByLoginName(dto.getUsername());
        if (ObjectUtil.isNull(adminUser)) {
            throw new BusinessException("用户名或密码错误");
        }
        this.validatePassword(adminUser, dto.getPassword());
        this.validateAccountStatus(adminUser);
        StpKit.ADMIN.login(adminUser.getId());

        CacheUserSession cache = this.getCacheUserSession(adminUser);
        UnifySession session = UserContextHolder.getSession();
        session.copyCommon(cache);
        UserContextHolder.setSession(session);
        StpKit.ADMIN.getSessionByLoginId(adminUser.getId(), true)
            .set(GlobalConstant.CACHE_CURRENT_ADMINISTRATOR_USER, cache);
        this.initPwdResetTimeIfFirstLogin(adminUser);
        this.updateLastLoginTime(adminUser.getId());
        return StpUtil.getTokenValue();
    }

    private void initPwdResetTimeIfFirstLogin(AdminUserEntity adminUser) {
        if (ObjectUtil.isNull(adminUser.getPwdResetTime())) {
            adminUser.setPwdResetTime(new Date());
            this.updateById(adminUser);
        }
    }

    @Override
    public AdminVO getAdminInfo(Integer userId) {
        AdminUserEntity entity = super.getById(userId);
        AdminVO vo = new AdminVO();
        if (ObjectUtil.isNotNull(entity)) {
            vo.setId(entity.getId());
            vo.setUsername(entity.getLoginName());
            vo.setNickname(entity.getActualName());
            vo.setEmail(entity.getEmail());
            vo.setCreateTime(entity.getCreatedTime());
            vo.setRegistrationDate(entity.getCreatedTime());
            vo.setRoles(new HashSet<>());
            vo.setDeptId(entity.getDeptId());
            AdminDeptEntity deptEntity = adminDeptService.getById(entity.getDeptId());
            if (ObjectUtil.isNotNull(deptEntity)) {
                vo.setDeptName(deptEntity.getName());
            }
            vo.setPermissions(this.getPermissionList(entity));
            vo.setPwdExpired(false);
        }
        return vo;
    }

    @Override
    public List<AdminMenuVO> getUserRoute(Integer userId) {
        List<MenuInfoBO> allMenuList = adminMenuService.menuList();
        if (!UserContextHolder.isAdmin()) {
            Map<Integer, List<Integer>> userMenu = adminUserMenuService.getUserMenu();
            List<Integer> userMenuIds = userMenu.get(userId);
            if (CollUtil.isNotEmpty(userMenuIds)) {
                allMenuList = allMenuList.stream()
                    .filter(menu -> userMenuIds.contains(menu.getId()))
                    .toList();
            }
        }
        List<AdminMenuVO> voList = new ArrayList<>();
        for (MenuInfoBO menuBO : allMenuList) {
            AdminMenuVO adminMenuVO = BeanUtil.copyProperties(menuBO, AdminMenuVO.class);
            voList.add(adminMenuVO);
        }
        return voList;
    }

    private Set<String> getPermissionList(AdminUserEntity entity) {
        List<PermissionBO> permissionBOList = adminMenuService.permissionList();
        Set<String> permissionList = new HashSet<>();
        if (BooleanUtil.isFalse(entity.getAdministratorFlag())) {
            Map<Integer, List<Integer>> userMenu = adminUserMenuService.getUserMenu();
            // 普通用户：根据用户菜单权限获取对应的权限码
            List<Integer> userMenuIds = userMenu.get(entity.getId());
            if (CollUtil.isNotEmpty(userMenuIds)) {
                // 从权限列表中筛选出用户有权限的菜单对应的权限码
                for (PermissionBO permission : permissionBOList) {
                    if (userMenuIds.contains(permission.getParentId())) {
                        permissionList.add(permission.getCode());
                    }
                }
            }
        } else {
            // 超级管理员：拥有所有权限
            permissionList.add(GlobalConstant.ALL_CODE);
        }
        return permissionList;
    }

    private CacheUserSession getCacheUserSession(AdminUserEntity adminUser) {
        CacheUserSession cache = new CacheUserSession();
        cache.setUserId(adminUser.getId().toString());
        cache.setTenantId(GlobalConstant.Number.NUMBER_0);
        cache.setAdministratorFlag(adminUser.getAdministratorFlag());
        cache.setLanguage(Locale.SIMPLIFIED_CHINESE.getLanguage());
        cache.setLoginName(adminUser.getLoginName());
        cache.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        cache.setZoneIdStr("Asia/Shanghai");
        cache.setAppendTimezoneStyle(false);
        return cache;
    }

    /**
     * 验证验证码
     */
    private void validateCaptcha(String captcha, String uuid) {
        if (StrUtil.isBlank(captcha) || StrUtil.isBlank(uuid)) {
            throw new BusinessException("验证码不能为空");
        }
        captchaService.checkCaptcha(captcha, uuid);
    }

    /**
     * 验证密码
     */
    private void validatePassword(AdminUserEntity adminUser, String inputPassword) {
        // todo
        if (!PasswordUtil.matches(inputPassword, adminUser.getLoginPwd())) {
            throw new BusinessException("用户名或密码错误");
        }
    }

    /**
     * 验证账号状态
     */
    private void validateAccountStatus(AdminUserEntity adminUser) {
        if (BooleanUtil.isTrue(adminUser.getInactive())) {
            throw new BusinessException("账号已被禁用");
        }
    }

    /**
     * 更新最后登录时间
     */
    private void updateLastLoginTime(Integer userId) {
        AdminUserEntity updateEntity = new AdminUserEntity();
        updateEntity.setId(userId);
        updateEntity.setLastLoginTime(new Date());
        this.updateById(updateEntity);
    }

    private AdminUserEntity getUserByLoginName(String loginName) {
        LambdaQueryWrapper<AdminUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUserEntity::getLoginName, loginName);
        return this.getOne(queryWrapper);
    }
}
