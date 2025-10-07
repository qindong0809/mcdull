package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUserSession;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.security.PasswordUtil;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.UserSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminDeptEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminRoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.*;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminUserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICaptchaService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private IAdminRoleService adminRoleService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String auth(LogonDTO dto) {
        this.validateCaptcha(dto.getCaptcha(), dto.getUuid());
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
            vo.setGender(entity.getGender());
            vo.setPhone(entity.getPhone());
            vo.setEmail(entity.getEmail());
            vo.setCreateTime(entity.getCreatedTime());
            vo.setRegistrationDate(entity.getCreatedTime());
            String roleJoin = entity.getRoleJoin();
            if (StrUtil.isNotBlank(roleJoin)) {
                String[] roleArray = roleJoin.split(",");
                List<Integer> roleIdList = new ArrayList<>();
                for (String roleId : roleArray) {
                    roleIdList.add(Integer.valueOf(roleId));
                }
                List<AdminRoleEntity> roleList = adminRoleService.listByIds(roleIdList);
                vo.setRoles(roleList.stream().map(AdminRoleEntity::getCode).collect(Collectors.toSet()));
                vo.setRoleNames(roleList.stream().map(AdminRoleEntity::getName).collect(Collectors.toList()));
            }
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

    @Override
    public Map<Integer, String> getUserNameMap() {
        List<AdminUserEntity> list = super.list();
        return list.stream().collect(Collectors.toMap(AdminUserEntity::getId, AdminUserEntity::getActualName));
    }

    @Override
    public PagedVO<AdminUserVO> getUserList(Integer pageNum, Integer pageSize, Integer deptId, Integer status, String createTime, String description, Integer roleId, Integer notInRoleId) {
        LambdaQueryWrapper<AdminUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<AdminDeptEntity> deptList = adminDeptService.list();
        if (ObjectUtil.isNotNull(deptId)) {
            List<Integer> deptIdList = getChildDeptId(deptId, deptList);
            deptIdList.add(deptId);
            queryWrapper.in(AdminUserEntity::getDeptId, deptIdList);
        }
        if (ObjectUtil.isNotNull(status)) {
            queryWrapper.eq(AdminUserEntity::getInactive, ObjectUtil.equal(status, 2));
        }
        if (StrUtil.isNotBlank(description)) {
            queryWrapper.and(i -> i.like(AdminUserEntity::getActualName, description)
                .or().like(AdminUserEntity::getLoginName, description));
        }
        if (StrUtil.isNotBlank(createTime)) {
            String[] split = createTime.split(",");
            String startTime = split[0];
            String endTime = split[1];
            queryWrapper.between(AdminUserEntity::getCreatedTime, startTime, endTime);
        }

        Map<Integer, String> deptNameMap = adminDeptService.getDeptNameMap();
        List<AdminUserVO> voList = new ArrayList<>();
        List<AdminUserEntity> list = baseMapper.selectList(queryWrapper);
        if (ObjectUtil.isNotNull(roleId)) {
            list = list.stream().filter( i -> {
                String roleJoin = i.getRoleJoin();
                if (StrUtil.isBlank(roleJoin)) {
                    return false;
                }
                List<String> split = StrUtil.split(roleJoin, ',');
                return split.contains(roleId.toString());
            }).collect(Collectors.toList());
        }
        if (ObjectUtil.isNotNull(notInRoleId)) {
            list = list.stream().filter( i -> {
                String roleJoin = i.getRoleJoin();
                if (StrUtil.isBlank(roleJoin)) {
                    return true;
                }
                List<String> split = StrUtil.split(roleJoin, ',');
                return !split.contains(notInRoleId.toString());
            }).collect(Collectors.toList());
        }

        Set<String> collect = list.stream().map(AdminUserEntity::getRoleJoin).collect(Collectors.toSet());
        Map<Integer, String> roleNameMap = new HashMap<>();
        List<Integer> roleIdList = new ArrayList<>();
        for (String roleJoin : collect) {
            String[] split = roleJoin.split(",");
            roleIdList.addAll(Arrays.stream(split).map(Integer::parseInt).toList());
            List<AdminRoleEntity> roleList = adminRoleService.listByIds(roleIdList);
            roleNameMap = roleList.stream().collect(Collectors.toMap(AdminRoleEntity::getId, AdminRoleEntity::getName));
        }
        Map<Integer, String> userNameMap = this.getUserNameMap();
        for (AdminUserEntity entity : list) {
            AdminUserVO vo = new AdminUserVO();
            vo.setId(entity.getId());
            vo.setUsername(entity.getLoginName());
            vo.setNickname(entity.getActualName());
            vo.setGender(entity.getGender());
            vo.setPhone(entity.getPhone());
            vo.setEmail(entity.getEmail());
            vo.setCreateTime(entity.getCreatedTime());
            vo.setDeptId(entity.getDeptId());
            vo.setDeptName(ObjectUtil.isNotNull(entity.getDeptId()) ? deptNameMap.get(entity.getDeptId()) : StrUtil.EMPTY);
            vo.setIsSystem(entity.getAdministratorFlag());
            vo.setStatus(entity.getInactive() ? 2 : 1);
            String[] split = entity.getRoleJoin().split(",");
            List<String> roleNameList = new ArrayList<>();
            List<Integer> rList = new ArrayList<>();
            for (String s : split) {
                roleNameList.add(roleNameMap.get(Integer.valueOf(s)));
                rList.add(Integer.valueOf(s));
            }
            vo.setRoleNames(roleNameList);
            vo.setRoleIds(rList);
            vo.setCreateUserString(userNameMap.get(entity.getCreatedBy()));
            vo.setUpdateUserString(userNameMap.get(entity.getUpdatedBy()));
            vo.setDescription(entity.getDescription());
            vo.setDisabled(entity.getAdministratorFlag());
            voList.add(vo);
        }
        return PageUtil.of(voList, pageSize, pageNum);
    }

    @Override
    public AdminUserSimpleVO getUser(Integer id) {
        AdminUserEntity entity = super.getById(id);
        AdminUserSimpleVO vo = new AdminUserSimpleVO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getLoginName());
        vo.setNickname(entity.getActualName());
        vo.setEmail(entity.getEmail());
        vo.setCreateTime(entity.getCreatedTime());
        vo.setUpdateTime(entity.getUpdatedTime());
        if (ObjectUtil.isNotNull(entity.getDeptId())) {
            AdminDeptEntity deptEntity = adminDeptService.getById(entity.getDeptId());
            vo.setDeptId(deptEntity.getId());
            vo.setDeptName(deptEntity.getName());
        }
        String[] split = entity.getRoleJoin().split(",");
        List<Integer> roleIds = new ArrayList<>();
        for (String s : split) {
            roleIds.add(Integer.parseInt(s));
        }
        vo.setRoleIds(roleIds);
        vo.setRoleNames(adminRoleService.listByIds(vo.getRoleIds()).stream().map(AdminRoleEntity::getName).toList());
        vo.setIsSystem(entity.getAdministratorFlag());
        vo.setStatus(entity.getInactive() ? 2 : 1);
        Map<Integer, String> userNameMap = this.getUserNameMap();
        vo.setCreateUserString(userNameMap.get(entity.getCreatedBy()));
        vo.setUpdateUserString(userNameMap.get(entity.getUpdatedBy()));
        vo.setDescription(entity.getDescription());
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveUser(UserSaveDTO dto) {
        AdminUserEntity entity = new AdminUserEntity();
        entity.setLoginName(dto.getUsername());
        entity.setActualName(dto.getNickname());
        entity.setLoginPwd(PasswordUtil.encode(dto.getPassword()));
        entity.setGender(dto.getGender());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setDeptId(dto.getDeptId());
        entity.setInactive(ObjectUtil.equal(dto.getStatus(), 2));
        entity.setAdministratorFlag(false);
        entity.setRoleJoin(StrUtil.join(",", dto.getRoleIds()));
        entity.setDescription(dto.getDescription());
        super.save(entity);
        return entity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean editUser(Integer id, UserSaveDTO dto) {
        AdminUserEntity entity = super.getById(id);
        if (ObjectUtil.isNotNull(entity)) {
            entity.setLoginName(dto.getUsername());
            entity.setActualName(dto.getNickname());
            entity.setGender(dto.getGender());
            entity.setPhone(dto.getPhone());
            entity.setEmail(dto.getEmail());
            entity.setDeptId(dto.getDeptId());
            entity.setInactive(ObjectUtil.equal(dto.getStatus(), 2));
            entity.setRoleJoin(StrUtil.join(",", dto.getRoleIds()));
            entity.setDescription(dto.getDescription());
            super.updateById(entity);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteUser(List<Integer> ids) {
        super.removeByIds(ids);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean editUserRole(Integer id, List<Integer> roleIds) {
        AdminUserEntity entity = super.getById(id);
        if (ObjectUtil.isNotNull(entity)) {
            entity.setRoleJoin(StrUtil.join(",", roleIds));
            super.updateById(entity);
        }
        return true;
    }

    private List<Integer> getChildDeptId(Integer deptId, List<AdminDeptEntity> deptList) {
        List<Integer> childerDeptId = new ArrayList<>();
        for (AdminDeptEntity dept : deptList) {
            if (dept.getParentId().equals(deptId)) {
                childerDeptId.add(dept.getId());
                childerDeptId.addAll(getChildDeptId(dept.getId(), deptList));
            }
        }
        return childerDeptId;
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
