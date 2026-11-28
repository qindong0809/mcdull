package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteLogonUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthApiDefImplTest {

    private AuthApiDefImpl controller;
    private ILoginService loginService;
    private IUserService userService;

    @BeforeEach
    void setUp() {
        controller = new AuthApiDefImpl();
        loginService = mock(ILoginService.class);
        userService = mock(IUserService.class);
        ICache redissonCache = mock(ICache.class);

        ReflectionTestUtils.setField(controller, "loginService", loginService);
        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "redissonCache", redissonCache);

        when(redissonCache.getListOrSetPrefixed(anyString(), eq(String.class),
                ArgumentMatchers.<Supplier<List<String>>>any(), anyLong(), eq(false)))
                .thenAnswer(invocation -> {
                    Supplier<List<String>> supplier = invocation.getArgument(2);
                    return supplier.get();
                });
    }

    @Test
    void adminGetsAllPermissionsFromDatabaseFlag() {
        UserEntity admin = new UserEntity();
        admin.setAdministratorFlag(true);
        when(userService.get(1)).thenReturn(admin);
        when(loginService.getRoleList(1)).thenReturn(List.of("admin"));

        InnerResult<RemoteLogonUserVO> result = controller.getLogonUserInfo(1, "admin");

        assertTrue(result.isOk());
        assertEquals(List.of("*:*:*"), result.getData().getPermissionList());
        assertEquals(List.of("admin"), result.getData().getRoleList());
        verify(loginService, never()).getPermissionList(1);
    }

    @Test
    void normalUserGetsPermissionAndRoleLists() {
        UserEntity user = new UserEntity();
        user.setAdministratorFlag(false);
        when(userService.get(2)).thenReturn(user);
        when(loginService.getPermissionList(2)).thenReturn(List.of("user:list"));
        when(loginService.getRoleList(2)).thenReturn(List.of("user"));

        InnerResult<RemoteLogonUserVO> result = controller.getLogonUserInfo(2, "login");

        assertTrue(result.isOk());
        assertEquals(List.of("user:list"), result.getData().getPermissionList());
        assertEquals(List.of("user"), result.getData().getRoleList());
    }

    @Test
    void invalidLoginIdReturnsError() {
        InnerResult<RemoteLogonUserVO> result = controller.getLogonUserInfo("abc", "login");

        assertFalse(result.isOk());
        verify(userService, never()).get(anyInt());
    }
}
