package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;

public interface ITenantAuthService {
    List<TenantUserVO> listUsers(Integer tenantId);

    void addUsers(Integer tenantId, List<Integer> userIds);

    void removeUsers(Integer tenantId, List<Integer> userIds);

    List<MenuTreeVO> menuTree(Integer tenantId, String applicationCode);

    void saveMenus(Integer tenantId, List<Integer> menuIds);

    Set<Integer> authorizedTenantIds(Integer userId);

    Set<Integer> allActiveTenantIds();

    List<TenantVO> tenantList(Set<Integer> tenantIds);

    List<String> applicationCodes(Integer tenantId);

    Set<Integer> menuIds(Integer tenantId);

    Integer currentTenantId(HttpServletRequest request);

    void validateCurrentTenant(Integer tenantId);
}
