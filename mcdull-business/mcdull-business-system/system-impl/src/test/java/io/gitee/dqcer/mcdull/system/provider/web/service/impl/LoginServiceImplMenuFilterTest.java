package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginServiceImplMenuFilterTest {

    private LoginServiceImpl service;

    private ITenantAuthService tenantAuthService;

    @BeforeEach
    void setUp() {
        service = new LoginServiceImpl();
        tenantAuthService = mock(ITenantAuthService.class);
        ReflectionTestUtils.setField(service, "tenantAuthService", tenantAuthService);
        when(tenantAuthService.authorizedTenantIds(anyInt())).thenReturn(Set.of(12));
    }

    @Test
    void emptyWhitelistKeepsOnlyPlatformMenus() {
        MenuVO tenantMenu = menu(1, "workflow");
        MenuVO platformMenu = menu(2, "system");
        when(tenantAuthService.applicationCodes(12)).thenReturn(List.of("workflow"));
        when(tenantAuthService.menuIds(12)).thenReturn(Collections.emptySet());

        List<MenuVO> result = filter(List.of(tenantMenu, platformMenu));

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getMenuId());
    }

    @Test
    void noMatchedTenantMenuReturnsEmptyInsteadOfAllMenus() {
        MenuVO tenantMenu = menu(1, "workflow");
        when(tenantAuthService.applicationCodes(12)).thenReturn(List.of("hr"));
        when(tenantAuthService.menuIds(12)).thenReturn(Set.of(1));

        List<MenuVO> result = filter(List.of(tenantMenu));

        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private List<MenuVO> filter(List<MenuVO> menus) {
        UserEntity user = new UserEntity();
        user.setId(1);
        return (List<MenuVO>) ReflectionTestUtils.invokeMethod(
                service, "filterMenuByTenant", menus, 12, user);
    }

    private MenuVO menu(Integer id, String applicationCode) {
        MenuVO vo = new MenuVO();
        vo.setMenuId(id);
        vo.setApplicationCode(applicationCode);
        return vo;
    }
}
