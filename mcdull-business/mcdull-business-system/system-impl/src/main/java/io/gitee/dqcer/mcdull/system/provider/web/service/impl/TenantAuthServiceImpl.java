package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantUserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.TenantInitStatusEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.TenantCacheEvictManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMenuMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantUserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.UserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantAuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TenantAuthServiceImpl implements ITenantAuthService {

    @Resource
    private TenantUserMapper tenantUserMapper;
    @Resource
    private TenantMenuMapper tenantMenuMapper;
    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private IMenuService menuService;
    @Resource
    private TenantCacheEvictManager cacheEvictManager;

    @Override
    public List<TenantUserVO> listUsers(Integer tenantId) {
        Set<Integer> userIds = tenantUserMapper.selectUserIdsByTenantId(tenantId);
        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, UserEntity> userMap = userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, e -> e, (a, b) -> a));
        LambdaQueryWrapper<TenantUserEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantUserEntity::getTenantId, tenantId).eq(TenantUserEntity::getDelFlag, false);
        return tenantUserMapper.selectList(query).stream().map(rel -> {
            UserEntity user = userMap.get(rel.getUserId());
            TenantUserVO vo = new TenantUserVO();
            vo.setUserId(rel.getUserId());
            vo.setJoinedTime(rel.getCreatedTime());
            if (user != null) {
                vo.setLoginName(user.getLoginName());
                vo.setActualName(user.getActualName());
                vo.setDepartmentId(user.getDepartmentId());
                vo.setInactive(user.getInactive());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUsers(Integer tenantId, List<Integer> userIds) {
        Set<Integer> exists = tenantUserMapper.selectUserIdsByTenantId(tenantId);
        for (Integer userId : userIds) {
            if (exists.contains(userId)) {
                continue;
            }
            TenantUserEntity entity = new TenantUserEntity();
            entity.setTenantId(tenantId);
            entity.setUserId(userId);
            tenantUserMapper.insert(entity);
            cacheEvictManager.clearUser(userId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUsers(Integer tenantId, List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        tenantUserMapper.deleteByTenantAndUsers(tenantId, userIds);
        userIds.forEach(cacheEvictManager::clearUser);
    }

    @Override
    public List<MenuTreeVO> menuTree(Integer tenantId, String applicationCode) {
        Set<Integer> checked = tenantMenuMapper.selectMenuIdsByTenantId(tenantId);
        List<MenuTreeVO> tree = menuService.queryMenuTree(false, applicationCode);
        return filterAndMark(tree, applicationCode, checked);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenus(Integer tenantId, List<Integer> menuIds) {
        tenantMenuMapper.deleteByTenantId(tenantId);
        if (menuIds != null) {
            for (Integer menuId : menuIds) {
                TenantMenuEntity entity = new TenantMenuEntity();
                entity.setTenantId(tenantId);
                entity.setMenuId(menuId);
                tenantMenuMapper.insert(entity);
            }
        }
        cacheEvictManager.clearTenant(tenantId, tenantUserMapper.selectUserIdsByTenantId(tenantId));
    }

    @Override
    public Set<Integer> authorizedTenantIds(Integer userId) {
        Set<Integer> ids = tenantUserMapper.selectTenantIdsByUserId(userId);
        if (ids.isEmpty()) {
            return ids;
        }
        return tenantMapper.selectList(activeTenantQuery().in(TenantEntity::getId, ids)).stream()
                .map(TenantEntity::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> allActiveTenantIds() {
        return tenantMapper.selectList(activeTenantQuery()).stream()
                .map(TenantEntity::getId).collect(Collectors.toSet());
    }

    @Override
    public List<TenantVO> tenantList(Set<Integer> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TenantEntity> query = activeTenantQuery().in(TenantEntity::getId, tenantIds);
        return tenantMapper.selectList(query).stream().map(entity -> {
            TenantVO vo = new TenantVO();
            vo.setId(entity.getId());
            vo.setTenantName(entity.getTenantName());
            vo.setOwnerType(entity.getOwnerType());
            vo.setIsolationMode(entity.getIsolationMode());
            vo.setApplicationCode(entity.getApplicationCode() == null ? Collections.emptyList()
                    : JSON.parseArray(entity.getApplicationCode(), String.class));
            vo.setExpireTime(entity.getExpireTime());
            vo.setInitStatus(entity.getInitStatus());
            vo.setInactive(entity.getInactive());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> applicationCodes(Integer tenantId) {
        TenantEntity tenant = tenantMapper.selectById(tenantId);
        if (tenant == null || tenant.getApplicationCode() == null || tenant.getApplicationCode().isBlank()) {
            return Collections.emptyList();
        }
        return JSON.parseArray(tenant.getApplicationCode(), String.class);
    }

    @Override
    public Set<Integer> menuIds(Integer tenantId) {
        return tenantMenuMapper.selectMenuIdsByTenantId(tenantId);
    }

    @Override
    public Integer currentTenantId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String value = request.getHeader(HttpHeaderConstants.T_ID);
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new BusinessException("system.tenant.required");
        }
    }

    @Override
    public void validateCurrentTenant(Integer tenantId) {
        if (tenantId == null || tenantId <= 0) {
            throw new BusinessException("system.tenant.required");
        }
        UnifySession session = UserContextHolder.getSession();
        if (session == null || StrUtil.isBlank(session.getUserId())) {
            throw new BusinessException("system.tenant.forbidden");
        }
        if (Boolean.TRUE.equals(session.getAdministratorFlag())) {
            return;
        }
        Set<Integer> authorized = session.getTenantIds();
        if (authorized == null || authorized.isEmpty()) {
            authorized = this.authorizedTenantIds(Integer.valueOf(session.getUserId()));
        }
        if (authorized == null || !authorized.contains(tenantId)) {
            throw new BusinessException("system.tenant.forbidden");
        }
    }

    private LambdaQueryWrapper<TenantEntity> activeTenantQuery() {
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantEntity::getDelFlag, false)
             .eq(TenantEntity::getInactive, false)
             .eq(TenantEntity::getInitStatus, TenantInitStatusEnum.SUCCESS.getCode())
             .and(w -> w.isNull(TenantEntity::getExpireTime)
                     .or()
                     .isNotNull(TenantEntity::getExpireTime).gt(TenantEntity::getExpireTime, new Date()));
        return query;
    }

    private List<MenuTreeVO> filterAndMark(List<MenuTreeVO> tree, String applicationCode, Set<Integer> checked) {
        if (tree == null) {
            return Collections.emptyList();
        }
        List<MenuTreeVO> result = new ArrayList<>();
        for (MenuTreeVO node : tree) {
            if (applicationCode != null && !applicationCode.isEmpty()
                    && !applicationCode.equals(node.getApplicationCode())) {
                continue;
            }
            node.setChecked(checked.contains(node.getMenuId()));
            node.setChildren(filterAndMark(node.getChildren(), applicationCode, checked));
            result.add(node);
        }
        return result;
    }
}
