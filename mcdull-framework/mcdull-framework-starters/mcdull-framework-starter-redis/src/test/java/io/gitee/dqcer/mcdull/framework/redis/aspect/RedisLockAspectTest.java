package io.gitee.dqcer.mcdull.framework.redis.aspect;

import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisLockAspectTest {

    private static final String KEY = "k";

    private RedisLockAspect aspect;
    private RLock lock;

    @BeforeEach
    void setUp() {
        RedissonClient redissonClient = mock(RedissonClient.class);
        lock = mock(RLock.class);
        when(redissonClient.getLock(KEY)).thenReturn(lock);
        aspect = new RedisLockAspect();
        ReflectionTestUtils.setField(aspect, "redissonClient", redissonClient);
    }

    @AfterEach
    void clearInterruptFlag() {
        Thread.interrupted();
    }

    @Test
    void lockFailureThrowsBusinessExceptionByDefault() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("defaultFailure");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> aspect.around(joinPoint));

        assertEquals(I18nConstants.SYSTEM_BUSY, exception.getMessageCode());
        assertNull(exception.getCause());
        verify(joinPoint, never()).proceed();
        verify(lock, never()).unlock();
    }

    @Test
    void lockFailureCanBeSilentlySkippedWhenOptedOut() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("optOutFailure");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenReturn(false);

        assertNull(aspect.around(joinPoint));

        verify(joinPoint, never()).proceed();
    }

    @Test
    void customMessageCodeIsHonored() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("customCode");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> aspect.around(joinPoint));

        assertEquals("custom.code", exception.getMessageCode());
    }

    @Test
    void interruptedAcquisitionRestoresFlagAndThrowsBusinessException() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("defaultFailure");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenThrow(new InterruptedException("boom"));

        BusinessException exception = assertThrows(BusinessException.class, () -> aspect.around(joinPoint));

        assertEquals(I18nConstants.SYSTEM_BUSY, exception.getMessageCode());
        assertInstanceOf(InterruptedException.class, exception.getCause());
        assertTrue(Thread.currentThread().isInterrupted());
        verify(joinPoint, never()).proceed();
    }

    @Test
    void successfulLockProceedsAndReleases() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("defaultFailure");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("done");

        assertEquals("done", aspect.around(joinPoint));

        verify(joinPoint).proceed();
        verify(lock).unlock();
    }

    @Test
    void unlockIsSkippedWhenLockIsNoLongerHeld() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("defaultFailure");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(false);
        when(joinPoint.proceed()).thenReturn("done");

        assertEquals("done", aspect.around(joinPoint));

        verify(lock, never()).unlock();
    }

    @Test
    void targetExceptionIsPropagatedAndLockReleased() throws Throwable {
        ProceedingJoinPoint joinPoint = joinPoint("defaultFailure");
        when(lock.tryLock(1, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        BusinessException failure = new BusinessException("biz.error");
        when(joinPoint.proceed()).thenThrow(failure);

        BusinessException thrown = assertThrows(BusinessException.class, () -> aspect.around(joinPoint));

        assertSame(failure, thrown);
        verify(lock).unlock();
    }

    private ProceedingJoinPoint joinPoint(String methodName) throws NoSuchMethodException {
        Method method = Target.class.getDeclaredMethod(methodName, String.class);
        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(signature.toLongString()).thenReturn(method.getName());

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getArgs()).thenReturn(new Object[]{KEY});
        return joinPoint;
    }

    static class Target {

        @RedisLock(key = "#key", timeout = 1)
        void defaultFailure(String key) {
        }

        @RedisLock(key = "#key", timeout = 1, throwOnFailure = false)
        void optOutFailure(String key) {
        }

        @RedisLock(key = "#key", timeout = 1, messageCode = "custom.code")
        void customCode(String key) {
        }
    }
}
